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

    var formData = App.shortenForm.serialize();
    if (App.isValidForm(formData)) {
      var promise = App.sendLink(formData);
      promise.done(function(data) {
        var parsed = JSON.parse(data);
        var shortUrlWithDomain = window.location.origin + '/' + parsed.shortURL;
        App.shortenForm.val(shortUrlWithDomain).select();

      });
    }

    App.shortenButton.hide();
    App.copyButton.show();
  },

  copyButtonHandler: function(event) {
    event.clipboardData.setData('text/plain', App.shortenForm.val());
  },

  isValidForm: function(formData) {
    console.log('formData = ' + formData);
    // TO DO
    return formData.substring(5).length > 0;
  },

  sendLink: function (formData) {
    return $.ajax({
      method: "POST",
      context: App,
      url: "shorten",
      data: formData
    });
  }

};
