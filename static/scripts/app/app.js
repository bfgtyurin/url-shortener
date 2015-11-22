'use strict';
$(function() {
  App.init();
});

var App = {

  init: function() {
    App.cacheElements();
    App.bindEvents();
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
        var shortUrlWithDomain = window.location.origin + '/' + data.shortURL;
        App.shortenForm.val(shortUrlWithDomain).select();
      });

      App.shortenButton.hide();
      App.copyButton.show();
    }

  },

  copyButtonHandler: function(event) {
    event.clipboardData.setData('text/plain', App.shortenForm.val());
  },

  isValidForm: function(formData) {
    console.log('formData = ' + formData);
    var urlRegEx = /((([A-Za-z]{3,9}:(?:\/\/)?)(?:[\-;:&=\+\$,\w]+@)?[A-Za-z0-9\.\-]+|(?:www\.|[\-;:&=\+\$,\w]+@)[A-Za-z0-9\.\-]+)((?:\/[\+~%\/\.\w\-]*)?\??(?:[\-\+=&;%@\.\w]*)#?(?:[\.\!\/\\\w]*))?)/g;
    return urlRegEx.test(formData);
  },

  ajaxPostRequest: function (formData) {
    return $.ajax({
      method: "POST",
      context: App,
      url: "shorten",
      data: formData
    });
  }

};
