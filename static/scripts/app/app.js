'use strict';
$(function() {

  App.init();

});

var App = {

  init: function() {
    App.cacheElements();
    App.bindEvents();
    App.renderElements();
  },

  cacheElements: function() {
    App.shortenForm = $("#shortenForm");
    App.shortenButton = $("#shortenButton");
    App.copyButton = $("#copyButton");

    App.recentLinkContainer = $('#recentLinkContainer');
    App.recentLink = $("#recentLink");
    App.recentLinkShortUrl = $('#recentLink a.shortUrl');
    App.recentLinkFullUrl = $('#recentLink a.fullUrl');
    App.recentLinkClicksAmount = $('#recentLink a.clicksAmount');
    App.recentLinkSmallCopyButton = $('.smallCopyButton');

    App.existingLinksList = $("#existingLinksList");
    App.client = new ZeroClipboard(App.copyButton);
    App.smallClient = new ZeroClipboard($('.smallCopyButton'));
  },

  bindEvents: function() {
    App.shortenButton.on('click', App.shortenButtonHandler.bind(App));
    App.shortenForm.on('input', App.shortenFormInputHandler.bind(App));
    App.client.on('copy', App.copyButtonHandler.bind(App));
    App.smallClient.on('copy', App.smallCopyButtonHandler.bind(App));
  },

  // Handlers

  shortenFormInputHandler: function() {
    App.copyButton.hide();
    App.shortenButton.show();
  },

  shortenButtonHandler: function(event) {
    event.preventDefault();

    var formData = App.shortenForm.val();
    if (App.isValidForm(formData)) {
      var serializedFormData = App.shortenForm.serialize();
      var promise = App.ajaxPostRequest(serializedFormData);
      promise.done(function(data) {
        App.updateExistingLinksList(data);
        App.updateCookie(data);
        App.updateRecentLink(data);
        App.updateShortenForm(data);
      });

      App.shortenButton.hide();
      App.copyButton.show();
    }

  },

  copyButtonHandler: function(event) {
      event.clipboardData.setData('text/plain', App.shortenForm.val());
  },

  smallCopyButtonHandler: function(event) {
  // todo
  },

  // View render and updates

  renderElements: function() {
    if (document.cookie) {
      var linksPromise = App.ajaxGetExistingLinksRequest();
      App.renderExistingLinksList(linksPromise);
    }
  },

  renderExistingLinksList: function(linksPromise) {
    linksPromise.done(function(data) {
      console.log('renderExistingLinksList ' + data);
      data.forEach(function(link, i, arr) {
        App.existingLinksList.append(App.createExistingLinksListElement(link));
      });
    });
  },

  updateExistingLinksList: function(data) {
    if (App.isNewShortLink(data)) {
      App.existingLinksList.append(App.createExistingLinksListElement(data));
    }
  },

  updateShortenForm: function(data) {
    var shortUrlWithDomain = App.getUrlWithStr(data.shortUrl);
    App.shortenForm.val(shortUrlWithDomain).select();
  },

  updateRecentLink: function(data) {
    var shortUrlWithDomain = App.getUrlWithStr(data.shortUrl);

    App.recentLinkShortUrl.attr('href', shortUrlWithDomain);
    App.recentLinkShortUrl.text(shortUrlWithDomain);

    App.recentLinkFullUrl.attr('href', data.fullUrl);
    App.recentLinkFullUrl.text(data.title);

    App.recentLinkClicksAmount.text(data.clicks);

    App.recentLinkContainer.fadeIn(1000);
  },

  updateCookie: function(data) {
    var expires = App.getExpiresCookieString();
    var shortUrlCookieKeyValue = document.cookie.split(';')[0];
    var shortUrlCookieValue = shortUrlCookieKeyValue.substring('shortUrls='.length);
    if (App.isNewShortLink(data)) {
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
    var promise = App.ajaxGetUrlRequest(url);
    return promise;
  },

  // html

  createExistingLinksListElement: function(link) {
    return '<li class="list-group-item link-list-item">' +
           '<div><a class="shortUrl" href="' + link.shortUrl + '">' + App.getUrlWithStr(link.shortUrl) +
           '</a><button type="button" class="smallCopyButton btn btn-default btn-xs">COPY</button></div>' +
           '<div><span class="glyphicon glyphicon-stats pull-right" aria-hidden="true"></span>' +
           '<a class="fullUrl" href="' + link.fullUrl +'">' + link.title + '</a>' +
           '<a class="clicksAmount pull-right">' + link.clicks + '</a></div></li>';
  },

  // Ajax

  ajaxPostRequest: function (formData) {
    return $.ajax({
      method: "POST",
      context: App,
      url: "shorten",
      data: formData
    });
  },

  ajaxGetExistingLinksRequest: function (data) {
    return $.ajax({
      method: "GET",
      context: App,
      url: "shorten"
    });
  },

  // Utils

  getExpiresCookieString: function() {
    var d = new Date();
    d.setTime(d.getTime() + (30*24*60*60*1000));
    return '; expires=' + d.toUTCString();
  },

  getUrlWithStr: function(str) {
     return window.location.origin + '/' + str;
  }

};
