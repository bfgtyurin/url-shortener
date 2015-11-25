'use strict';
$(function() {
  App.init();
});

var App = {

  init: function() {
    App.setCookie();
    App.cacheElements();
    App.bindEvents();
    App.renderElements();
  },

  setCookie: function() {
    if (!document.cookie) {
      var expires = App.getExpiresCookieString();
      document.cookie = 'shortUrls=;' + expires;
    }
  },

  cacheElements: function() {
    App.shortenForm = $("#shortenForm");
    App.shortenButton = $("#shortenButton");
    App.copyButton = $("#copyButton");
    App.client = new ZeroClipboard(App.copyButton);
  },

  bindEvents: function() {
    App.shortenButton.on('click', App.shortenButtonHandler.bind(App));
    App.shortenForm.on('input', App.shortenFormInputHandler.bind(App));
    App.client.on('copy', App.copyButtonHandler.bind(App));
  },

  renderElements: function() {
    var isExistShortUrlsInCookie = document.cookie.split(';')[0].split('=')[1].length;
    if (isExistShortUrlsInCookie) {
      var linksPromise = App.getExistingLinksFromCookie();
      App.renderExistingLinksView(linksPromise);
    }
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
        App.updateCookie(data);
        App.updateFormView(data);
        App.updateRecentLinkView(data);
      });

      App.shortenButton.hide();
      App.copyButton.show();
    }

  },

  copyButtonHandler: function(event) {
      event.clipboardData.setData('text/plain', App.shortenForm.val());
  },

  // View render and updates

  updateFormView: function(data) {
    var shortUrlWithDomain = window.location.origin + '/' + data.shortURL;
    App.shortenForm.val(shortUrlWithDomain).select();
  },

  updateRecentLinkView: function(data) {

  },

  renderExistingLinksView: function(linksPromise) {
    linksPromise.done(function(data) {
      console.log('renderTableWithArray ' + data);
    });

  },

  updateCookie: function(data) {
    var expires = App.getExpiresCookieString();
    var tempLinks = document.cookie.split(';')[0];
    tempLinks += data.shortURL + ':';

    document.cookie = tempLinks + ';' + expires;
    console.log(document.cookie);
  },

  getExistingLinksFromCookie: function() {
    var shortLinks = document.cookie.split(';')[0].split('=')[1];
    console.log(shortLinks);
    var promise = App.ajaxGetRequest(shortLinks);

    return promise;
  },

  isValidForm: function(formData) {
    var urlRegEx = /((([A-Za-z]{3,9}:(?:\/\/)?)(?:[\-;:&=\+\$,\w]+@)?[A-Za-z0-9\.\-]+|(?:www\.|[\-;:&=\+\$,\w]+@)[A-Za-z0-9\.\-]+)((?:\/[\+~%\/\.\w\-]*)?\??(?:[\-\+=&;%@\.\w]*)#?(?:[\.\!\/\\\w]*))?)/g;
    return urlRegEx.test(formData);
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

  ajaxGetRequest: function (data) {
    return $.ajax({
      method: "GET",
      context: App,
      url: "shorten",
      data: data
    });
  },

  // Utils

  getExpiresCookieString: function() {
    var d = new Date();
    d.setTime(d.getTime() + (30*24*60*60*1000));
    return 'expires=' + d.toUTCString();
  }

};
