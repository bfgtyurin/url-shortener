'use strict';
$(function() {

  var App = {

    init: function() {
      this.cacheElements();
      this.bindEvents();

    },

    cacheElements: function() {
      this.shortenForm = $("#shortenForm");
      this.shortenButton = $("#shortenButton").addClass('shorten-button');
    },

    bindEvents: function() {
      this.shortenButton.on('click', this.shortenButtonHandler.bind(this));
      this.shortenForm.on('input', this.shortenFormInputHandler.bind(this));
    },

    shortenFormInputHandler: function() {
      this.initShortenButton();
    },

    initShortenButton: function() {
      this.shortenButton.removeClass('copy-button').addClass('shorten-button');
      this.shortenButton.text('SHORTEN');
    },

    shortenButtonHandler: function(event) {
      event.preventDefault();

      if (this.shortenButton.hasClass('shorten-button')) {

        var formData = this.shortenForm.serialize();
        if (this.isValidForm(formData)) {
          this.shortenButton.removeClass('shorten-button').addClass('copy-button');
          this.shortenButton.text('COPY');
          var promise = this.sendLink(formData);
          promise.done(function(data) {
            var parsed = JSON.parse(data);
            var shortUrlWithDomain = window.location.host + '/' + parsed.shortURL;
            this.shortenForm.val(shortUrlWithDomain).select();

          });
        }

      }
    },

    isValidForm: function(formData) {
      console.log('formData = ' + formData);
      // TO DO
      return formData.substring(5).length > 0;
    },

    sendLink: function (formData) {
      return $.ajax({
        method: "POST",
        context: this,
        url: "shorten",
        data: formData
      });
    }

  };

  App.init();

});
