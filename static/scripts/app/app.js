'use strict';
$(function() {

  App.init();

});

var App = {

  init: function() {
    this.cacheElements();
    this.bindEvents();
    this.renderElements();
  },

  cacheElements: function() {
    this.shortenForm = $("#shortenForm");
    this.shortenButton = $("#shortenButton");
    this.copyButton = $("#copyButton");

    this.recentLinkContainer = $('#recentLinkContainer');
    this.recentLink = $("#recentLink");
    this.recentLinkShortUrl = $('#recentLink a.shortUrl');
    this.recentLinkFullUrl = $('#recentLink a.fullUrl');
    this.recentLinkClicksAmount = $('#recentLink a.clicksAmount');
    this.recentLinkSmallCopyButton = $('.smallCopyButton');

    this.existingLinksList = $("#existingLinksList");
    this.client = new ZeroClipboard(this.copyButton);
    this.smallClient = new ZeroClipboard($('.smallCopyButton'));
  },

  bindEvents: function() {
    this.shortenButton.on('click', this.shortenButtonHandler.bind(this));
    this.shortenForm.on('input', this.shortenFormInputHandler.bind(this));
    this.client.on('copy', this.copyButtonHandler.bind(this));
    this.smallClient.on('copy', this.smallCopyButtonHandler.bind(this));
  },

  // Handlers

  shortenFormInputHandler: function() {
    this.copyButton.hide();
    this.shortenButton.show();
  },

  shortenButtonHandler: function(event) {
    event.preventDefault();

    var formData = this.shortenForm.val();
    if (this.isValidForm(formData)) {
      var serializedFormData = this.shortenForm.serialize();
      var promise = this.ajaxPostRequest(serializedFormData);
      promise.done(function(data) {
        this.updateExistingLinksList(data);
        this.updateCookie(data);
        this.updateRecentLink(data);
        this.updateShortenForm(data);

        this.shortenButton.hide();
        this.copyButton.show();
      });
    }

  },

  copyButtonHandler: function(event) {
      event.clipboardData.setData('text/plain', this.shortenForm.val());
  },

  smallCopyButtonHandler: function(event) {
  // todo
  },

  // View render and updates

  renderElements: function() {
    if (document.cookie) {
      var linksPromise = this.ajaxGetExistingLinksRequest();
      this.renderExistingLinksList(linksPromise);
    }
  },

  renderExistingLinksList: function(linksPromise) {
    linksPromise.done(function(data) {
      console.log('renderExistingLinksList ' + data);
      data.forEach(function(link, i, arr) {
        this.existingLinksList.append(this.createExistingLinksListElement(link));
      }, this);
    });
  },

  updateExistingLinksList: function(data) {
    if (this.isNewShortLink(data)) {
      this.existingLinksList.append(App.createExistingLinksListElement(data));
    }
  },

  updateShortenForm: function(data) {
    var shortUrlWithDomain = this.getUrlWithStr(data.shortUrl);
    this.shortenForm.val(shortUrlWithDomain).select();
  },

  updateRecentLink: function(data) {
    var shortUrlWithDomain = this.getUrlWithStr(data.shortUrl);

    this.recentLinkShortUrl.attr('href', shortUrlWithDomain);
    this.recentLinkShortUrl.text(shortUrlWithDomain);

    this.recentLinkFullUrl.attr('href', data.fullUrl);
    this.recentLinkFullUrl.text(data.title);

    this.recentLinkClicksAmount.text(data.clicks);

    this.recentLinkContainer.fadeIn(1000);
  },

  updateCookie: function(data) {
    var expires = this.getExpiresCookieString();
    var shortUrlCookieKeyValue = document.cookie.split(';')[0];
    var shortUrlCookieValue = shortUrlCookieKeyValue.substring('shortUrls='.length);
    if (this.isNewShortLink(data)) {
      shortUrlCookieValue += data.shortUrl + '/';
      document.cookie = 'shortUrls=' + shortUrlCookieValue + ';' + expires;
    }
  },

  isNewShortLink: function(data) {
    var value = data.shortUrl;
    var keyValueString = document.cookie.split(';')[0];
    var temp = keyValueString.substring("shortUrls=".length);
    var array = temp.split('/');

    return array.indexOf(value) === -1;
  },

  isValidForm: function(formData) {
    var urlRegEx = /((([A-Za-z]{3,9}:(?:\/\/)?)(?:[\-;:&=\+\$,\w]+@)?[A-Za-z0-9\.\-]+|(?:www\.|[\-;:&=\+\$,\w]+@)[A-Za-z0-9\.\-]+)((?:\/[\+~%\/\.\w\-]*)?\??(?:[\-\+=&;%@\.\w]*)#?(?:[\.\!\/\\\w]*))?)/g;
    return urlRegEx.test(formData);
  },

  getPageTitle: function(url) {
    var promise = this.ajaxGetUrlRequest(url);
    return promise;
  },

  // html

  createExistingLinksListElement: function(link) {
    return '<li class="list-group-item link-list-item">' +
           '<div><a class="shortUrl" href="' + link.shortUrl + '">' + this.getUrlWithStr(link.shortUrl) +
           '</a><button type="button" class="smallCopyButton btn btn-default btn-xs">COPY</button></div>' +
           '<div><span class="glyphicon glyphicon-stats pull-right" aria-hidden="true"></span>' +
           '<a class="fullUrl" href="' + link.fullUrl +'">' + link.title + '</a>' +
           '<a class="clicksAmount pull-right">' + link.clicks + '</a></div></li>';
  },

  // Ajax

  ajaxPostRequest: function (formData) {
    return $.ajax({
      method: "POST",
      context: this,
      url: "shorten",
      data: formData
    });
  },

  ajaxGetExistingLinksRequest: function (data) {
    return $.ajax({
      method: "GET",
      context: this,
      url: "shorten"
    });
  },

  // Utils

  getExpiresCookieString: function() {
    var expired = this.getExpiredDate({days: 30});

    return '; expires=' + expired;
  },

  getExpiredDate: function(period) {
    var dayInMillis = 24 * 60 * 60 * 1000;
    var monthInMillis = dayInMillis  * period.days;
    var date = new Date();
    date.setTime(date.getTime() + monthInMillis);

    return date.toUTCString();
  },

  getUrlWithStr: function(str) {
    return window.location.origin + '/' + str;
  }

};
