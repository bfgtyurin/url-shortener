'use strict';
$(function() {

  var App = {

    init: function() {
      this.cacheElements();
      this.bindEvents();
    },

    cacheElements: function() {
      this.shortenForm = $("#shortenForm");
      this.shortenButton = $("#shortenButton");
    },

    bindEvents: function() {
      this.shortenButton.on('click', this.shortenButtonHandler.bind(this));
    },

    shortenButtonHandler: function(event) {
      event.preventDefault();

      var promise = this.sendLink();
      promise.done(function(data) {
        this.shortenForm.val(data).select();
        this.shortenButton.text('COPY');
      });
    },

    sendLink: function () {
      return $.ajax({
        method: "POST",
        dataType: "text",
        context: this,
        url: "shorten",
        data: this.shortenForm.serialize()
      });
    }

  };

  App.init();

});
