var vm = new Vue({
    el:"#vm",
    data:{
			loadingFlag:false,
			phone : getStorage("JKGLSphone") || '',
			password : getStorage("JKGLSpassword") || '',
			isRemember : getStorage("JKGLSisRemember")==='true' || false//布尔值存储后会转化成字符串
    },
    computed:{
        disabled:function () {
            return !(this.phone && this.password);
        }
    },
    methods:{
        login:function () {
					this.loadingFlag=true;
					var _self = this;
					$.ajax( {
						type:"POST",
						url: urlPrefix+"/user/login.do",
						data:{phone:this.phone, password:this.password },
						success: function( data ) {
							console.log( data );
							if( data.user ) {
								setStorage("userId",data.user.id);
								setStorage("phone",data.user.phone);
								setStorage("nickname",data.user.nickname);
								setStorage("isAdmin",data.user.isAdmin);
								if(_self.isRemember){
									//账号密码保存在本地
									setStorage("JKGLSphone",_self.phone);
									setStorage("JKGLSpassword",_self.password);
								}else{
									//清除本地保存的账号密码
									deleteStorage(['JKGLSphone','JKGLSpassword']);
									deleteStorage(['myKeyword','myTypeId','publicKeyword','publicTypeId','customerListPage','publicCstomerListPage','userListPage']);
								}
								location.href=urlPrefix + "/sys/toindex.do";
							}
						},
						error: function( data ) {
							console.log( data.responseJON );
							_self.loadingFlag=false;
							layer.msg(data.responseJSON.message);
						}
					} );
				},
			keylogin:function (e) {
			if(e.keyCode === 13 && !this.disabled){
				this.login();
			}
		}
  },
	mounted:function(){
		watchLayuiCheckbox();
	}
});

function watchLayuiCheckbox(){
	var form = layui.form;
	//赋初始值
	form.val("loginForm", {
		  "isRemember": getStorage("JKGLSisRemember")==='true'
	});
	//更新渲染
	form.render("checkbox");
	//监听点击事件
	form.on('checkbox(isRemember)', function(data){
		  console.log(data.elem.checked); //是否被选中，true或者false
		 //console.log(data.value); //复选框value值，也可以通过data.elem.value得到
		  vm.isRemember=data.elem.checked;
		  setStorage('JKGLSisRemember',data.elem.checked);
	});
}


if(!Vue){
    history.go(0);
}
