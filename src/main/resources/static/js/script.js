console.log("This is Script File");
const toggleSidebar=()=>{
	
	if($(".sidebar").is(":visible"))
	{
		// if true ,have to hide the cross
		$(".sidebar").css("display","none");
		$(".content").css("margin-left","0%");
	}
	
	else
	{
		
		//if false ,have to show the cross
		$(".sidebar").css("display","block");
		$(".content").css("margin-left","20%");
	}	
};

