$(function() {
  //ページングタグにより自動生成される要素を削除している
  $('.true').remove();
});

$(function() {
  // ソート条件
  $("#sortKey,#sortDir").change(function() {
      $(this).parents('form').submit();
  });
});

$(function () {
  var $clientId = $('#client-id');
  var $clientName = $('#client-name');
  $('#client-remove').click(function (event) {
    // href属性に#を指定しているaタグに、ページ遷移を行わなせないようpreventDefault()を呼び出している
    event.preventDefault();
    $clientId.val('');
    $clientName.val('');
  })
});