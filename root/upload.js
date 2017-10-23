(function () {

    var uploadfiles = document.querySelector('#uploadfiles');
    uploadfiles.addEventListener('change', function () {
        var files = this.files;
        for(var i=0; i<files.length; i++){
            uploadFile(this.files[i]);
        }

    }, false);

    function uploadFile(file,welcomeFile){
    	console.log(file);
    	$.ajax({
    	    url: "../Upload",
    	    type: "POST",
    	    beforeSend: function(request) {
    	        request.setRequestHeader("Filename", file.name+","+file.type+","+file.size+","+welcomeFile);
    	      },
    	    data: file,
    	    processData: false,
    	    contentType: false,
    	    success: function (res){
    	    	console.log(res);
    	      //document.getElementById("response").innerHTML = res;
    	    }
    	  });
    	window.location.reload();
    }
}());