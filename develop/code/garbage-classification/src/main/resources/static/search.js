$(function () {

    getDate();

    function getDate() {
        var temp = '';
        $.ajax({
            type: "get",
            url: "http://192.168.88.105:8080/classify/byStr?garbageName=苹果",
            dataType: "json",
            success: function (res) {
                console.log(res)
                var list = res.data
                console.log(list)
                for (var $i = 0; $i < list.length; $i++) {
                    temp +=
                        '<tr>' +
                        '<td>' + list[$i].categoryExplain + '</td>' +
                        '<td>' + list[$i].garbageName + '</td>' +
                        '<td>' + list[$i].tip + '</td>' +
                        '</tr>';
                }
                $(".raymond").html(temp); /*  除了第一行tr的内容，其余内容清空，防止获取重复的数据  */
                $(".raymond").append($(temp).html());
            }
        });
    }
    $("a").click(function(){
    	var url = this.href;
    	console.log(url);
    })
});