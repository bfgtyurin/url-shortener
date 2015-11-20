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
      this.shortenForm.on('input', this.shortenFormHandler.bind(this));
    },

    shortenFormHandler: function() {
      this.shortenButton.removeClass('copy-button').addClass('shorten-button');
      this.shortenButton.text('SHORTEN');
    },

    shortenButtonHandler: function(event) {
      event.preventDefault();

      if (this.shortenButton.hasClass('shorten-button')) {
      this.shortenButton.removeClass('shorten-button').addClass('copy-button');
      this.shortenButton.text('COPY');
        var promise = this.sendLink();
        promise.done(function(data) {
          this.shortenForm.val(data).select();
        });
      }
    },

    sendLink: function () {
      return $.ajax({
        method: "POST",
        context: this,
        url: "shorten",
        data: this.shortenForm.serialize()
      });
    }

  };

  App.init();

});
