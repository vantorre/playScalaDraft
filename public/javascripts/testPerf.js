window.addEventListener('load', function() {

    testPerf();

});

function testPerf(){
    for(var i=0; i<5; i++){
        $.get("http://localhost:9000/message", function(data){
            callbackTestPerf(data)
        });

    }
}

function callbackTestPerf(data){
    $("#msg").html($("#msg").html() + '<li>' + data + '</li>');
}
