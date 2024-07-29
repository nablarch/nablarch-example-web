$(function() {
  var $clientId = $('#client-id');
  var $clientName = $('#client-name');
  
  $("#topUpdateButton").click(function() {
    $("#bottomUpdateButton").click();
  });

  $("#topCreateButton").click(function() {
    $("#bottomCreateButton").click();
  });

  $("#topDeleteButton").click(function() {
    $("#bottomDeleteButton").click();
  });

  $("#topBackButton").click(function() {
    $("#bottomBackButton").click();
  });

  $("#topSubmitButton").click(function() {
    $("#bottomSubmitButton").click();
  });

  $(".datepicker").datepicker();


  $('#client-remove').click(function (event) {
    event.preventDefault();
    $clientId.val('');
    $clientName.val('');
  })
});

