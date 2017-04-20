/**
 * Created by Errol on 2016/6/4.
 */

jQuery(document).ready(function(){
	
	var show_msg = function(message){
		jQuery("#errmsg").text(message);
		jQuery("#adminName").focus();
		jQuery("#submit").attr("disabled", false);
		setTimeout(function(){
			jQuery("#errmsg").text("");
		}, 2000);
	};
	
	jQuery("#password").keyup(function(e){
		if(e.which == 13){
			if(!jQuery("#submit").attr("disabled")){
				jQuery("#submit").trigger("click");
			}else{
				show_msg("请勿重复提交登录信息");
				jQuery("#submit").attr("disabled", true);
			}
		}
	});
	
	jQuery("#submit").click(function(){
		jQuery(this).attr("disabled", true);
		var a = jQuery("#adminName").val();
		var p = jQuery("#password").val();
		if(a=="" || p==""){
			show_msg("用户名和密码不能为空！");
		}else{
			jQuery.ajax({
				url : "admin/login",
				type : "post",
				data : {
					adminName : a,
					password : p
				},
				datatype : "json",
				success : function(res){
					if(res.code == 0){
						location.href = "dashboard";
					}else{
						show_msg("用户名或密码错误");
					}
				},
				error : function(xhr, status, error){
					show_msg("系统错误！请联系技术服务！");
				}
			});
		}
	});
	
});
