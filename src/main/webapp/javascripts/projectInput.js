$(function() {
  var $clientId = $('#client-id');
  var $clientName = $('#client-name');
  
  $("#topUpdateButton").click(function() {
    $("#bottomUpdateButton").click();
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
    // href属性に#を指定しているaタグに、ページ遷移を行わなせないようpreventDefault()を呼び出している
    event.preventDefault();
    $clientId.val('');
    $clientName.val('');
  })
});

