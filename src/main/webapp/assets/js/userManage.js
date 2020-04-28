var vm = new Vue({
	el:'#app',
	mixins:[myMixin],
	data:{
		pageName:'用户管理',//当前所在导航项的index

		keyword:'',
		userList:[],
		pageData:{currentPage:1,totalPage:0,total:0},

		mHideAdd:false,
		mHide:false,
		userPopTitle:'',
		newUser:{
			username:'',
			nickname:'',
			password:'',
			phone:'',
			isAdmin:''
		},
		rePassword:'',
		ifMRight:true,
		thisUserId:''//当前修改或删除的用户id
	},
	methods:{
		//获取用户列表
		getUserList:function(page){
			var _this=this;
			$.ajax( {
				type: "GET",
				url: urlPrefix+"/user/getuserlist.do",
				data: {
					keyword:this.keyword,
					currentPage:page,
					pageSize:10
				},
				success: function( data ) {
					console.log( data );
					if( data.userList ) {
						_this.userList = data.userList;
						_this.pageData = data;
					}
				},
				error: function( data ) {
					console.log( data.responseJSON );
					layer.msg(data.responseJSON.message);
				}
			} );
		},
		//出现添加用户弹窗
		showAddUserPop:function(){
			var _this=this;
			_this.mHide=true;
			_this.userPopTitle = '添加用户';
			_this.thisUserId='';
			_this.newUser={
				username:'',
				nickname:'',
				password:'',
				phone:'',
				isAdmin:''
			};
			_this.rePassword='';
			setTimeout('layui.form.render("select");',100);
		},
		//出现修改用户弹窗
		showModifyUserPop:function(item){
			var _this=this;
			_this.mHide=true;
			_this.userPopTitle = '修改用户';
			_this.thisUserId=item.id;
			//填充内容
			_this.newUser={
				username:item.username,
				nickname:item.nickname,
				password:'',
				phone:item.phone,
				isAdmin:item.isAdmin,
			};
			_this.rePassword='';
			defaultVal();
			setTimeout('layui.form.render("select");',100);

		},
		//关闭用户弹窗
		closeUserPop:function(){
			this.mHide=false;
		},
		//修改用户
		submitUser:function(){
			var _this=this;
			if(_this.newUser.nickname === '' || _this.newUser.nickname.trim().length == 0){
				layer.msg('请填写用户昵称');
				return;
			}
			if(_this.newUser.username === '' || _this.newUser.username.trim().length == 0){
				layer.msg('请填写用户真实姓名');
				return;
			}
			if(!/^1[0-9]{10}$/.test(_this.newUser.phone)){
				layer.msg('请填写正确的手机');
				return;
			}
			if(_this.newUser.isAdmin ===''){
				layer.msg('请选择角色');
				return;
			}
			if (_this.thisUserId === '') {
				if(_this.newUser.password === '' || _this.newUser.password.trim().length == 0){
					layer.msg('请填写密码');
					return;
				}
				if(_this.rePassword === '' || _this.rePassword.trim().length == 0){
					layer.msg('请确认密码');
					return;
				}
			}
			if(_this.newUser.password!==_this.rePassword){
				layer.msg('密码不一致');
				return;
			}
			if(_this.newUser.password!=='' && _this.rePassword!=='' && !/^[0-9a-zA-Z]{6,20}$/.test(_this.rePassword)){
				layer.msg('请输入6-20位数字或字母组成的密码');
				return;
			}
			var data = $.extend(_this.newUser,{id:_this.thisUserId});
			$.ajax( {
				type: "POST",
				url: urlPrefix+"/user/saveorupdate.do",
				data:data,
				success: function( data ) {
					console.log( data);
					if( data.saveorupdate ) {
						_this.closeUserPop();
						if (_this.thisUserId === '') {
							layer.msg('添加成功');
						} else {
							layer.msg('修改成功');
						}
						_this.getUserList(_this.pageData.currentPage);
					}
				},
				error: function( data ) {
					console.log( data.responseJON );
					myAlert({type:2,msg:data.responseJSON.message});
				}
			} );

		},
		//点击删除
		deleteBtn:function(id){
			this.thisUserId = id;
			myAlert({msg:'确定注销此用户吗？'},this.deleteUser);
		},
		//删除用户
		deleteUser:function(){
			var _this=this;
			$.ajax( {
				type: "POST",
				url: urlPrefix+"/user/delete.do",
				data:{id:_this.thisUserId},
				success: function( data ) {
					console.log( data);
					if( data.deleted == 1) {
						layer.msg('删除成功');
						_this.getUserList(1);
					}
				},
				error: function( data ) {
					console.log( data.responseJON );
					layer.msg(data.responseJSON.message);
				}
			} );
		},
		//分页组件绑定事件
		changePage:function(page){
			this.getUserList(page);
		},
		// 回车键触发查询
		keySearch:function (e) {
			if(e.keyCode === 13){
				this.getUserList(1);
			}
		}
	},
	mounted:function(){
		this.getUserList(1);
	},
	watch:{
		'newUser.mobile':function(){
			var reg = /^1[0-9]{10}$/;
			this.ifMRight=reg.test(this.newUser.mobile);
		}
	},
	components:{'m-page':mPage}
});

(function(  ){
	var form=layui.form;
	form.render('select');
	//监听医院的选择
	form.on('select(role)', function(data){
		vm.newUser.isAdmin = data.value; //得到被选中的值
	});

})();

//给select赋值
function defaultVal()
{
	layui.form.val('userPop',{
		"role": vm.newUser.isAdmin,
	})
}