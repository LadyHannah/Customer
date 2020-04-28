
//页面头部
Vue.component('my-header',{
	template:'<div class="layui-header indexHeader">' +
					 '<div class="layui-row headerBody">' +
					 '<div class="layui-col-xs6 left">' +
					 '<span class="name" @click="home">客户管理系统</span>' +
					 '</div>' +
					 '<div class="layui-col-xs6 right m-text-right">' +
					 '<span class="user">当前用户：{{userName}}</span>' +
					 '<span class="modifyPW" @click="modifyPW">修改密码</span>' +
					 '<span class="signOut" @click="exit">退出</span>' +
					 '</div>' +
					 '</div>' +
					 '</div>',
	data:function(){
		return {
			//orgName:getStorage('orgName')
		}
	},
	computed:{
		userName:function(){
			return userName=getStorage("nickname");
		}
	},
	methods:{
		exit:function () {
			var options={ "msg":"确定要退出登录吗？" };
			myAlert(options,layOut);
		},
		home:function () {
			location.href = urlPrefix+'/sys/toindex.do';
		},
		modifyPW:function(){
			this.$emit('showpw','block');
		}
	}
});

//左侧导航
Vue.component('left-bar',{
	template:'<div class="layui-side leftBar"> ' +
					 '<div class="layui-side-scroll"> ' +
					 '<ul class="layui-nav layui-nav-tree"> ' +
					 '<li class="layui-nav-item " @click="gotoOthers(1)" :class="{\'layui-this\':pageName===\'我的客户\'}"><a href="javascript:;">我的客户</a></li>' +
					 '<li class="layui-nav-item " @click="gotoOthers(2)" :class="{\'layui-this\':pageName===\'公海客户\'}"><a href="javascript:;">公海客户</a></li>' +
					 '<li class="layui-nav-item " @click="gotoOthers(3)" :class="{\'layui-this\':pageName===\'联系量统计\'}" v-if="isAdmin == 1"><a href="javascript:;">联系量统计</a></li>' +
					 '<li class="layui-nav-item " @click="gotoOthers(4)" :class="{\'layui-this\':pageName===\'用户管理\'}" v-if="isAdmin == 1"><a href="javascript:;">用户管理</a></li>' +
					/* '<li v-for="(item,index) in rights" v-show="item.see" @click="gotoOthers(item.authnName)" class="layui-nav-item" :class="{\'layui-this\':pageName===item.authnName}" :name="item.authnName"><a href="javascript:;">{{item.authnName}}</a></li> ' +*/
					 '</ul> ' +
					 '</div> ' +
					 '</div>',
	props:['pageName'],
	data:function(){
		return {
			isAdmin:getStorage('isAdmin')
		}
	},
	computed:{
		
	},
	methods:{
		gotoOthers:function(authnName){
			if(authnName===1){
				deleteStorage(['myKeyword','myTypeId','customerListPage']);
				location.href = urlPrefix+'/sys/toindex.do';
			}else if(authnName===2){
				deleteStorage(['publicKeyword','publicTypeId','publicCustomerListPage']);
				location.href = urlPrefix+'/sys/topubliccustomer.do';
			}else if(authnName===3){
				deleteStorage(['contactListPage']);
				location.href = urlPrefix+'/data/tocontactstatistics.do';
			}else if(authnName===4){
				deleteStorage(['userListPage']);
				location.href = urlPrefix+'/user/tousermanage.do';
			}
		}
	},
	mounted:function(){
		//console.log();
	}
});
//修改密码弹窗
Vue.component('modifypw-pop',{
	template:'<div class="myPop modifyPWPop" :style="{display:popDisplay}">' +
					 '<div class="alertBody">' +
					 '<div class="titleArea">' +
					 '<div class="title">修改密码</div>' +
					 '<div class="close" @click="close"></div>' +
					 '</div>' +
					 '<div class="layui-form contentArea">' +
					 '<div class="item">' +
					 '<label class="label">请输入原密码：</label>' +
					 '<input class="layui-input" v-model="oldPassword" type="password" placeholder="请输入">' +
					 '</div>' +
					 '<div class="item">' +
					 '<label class="label">请输入新密码：</label>' +
					 '<input class="layui-input" v-model="password" type="password" placeholder="请输入">' +
					 '</div>' +
					 '<div class="item">' +
					 '<label class="label">再次确认密码：</label>' +
					 '<input class="layui-input" v-model="rePassword" type="password" placeholder="请输入">' +
					 '</div>' +
					 '</div>' +
					 '<div class="btnArea">' +
					 '<div class="layui-btn layui-btn-normal yes" @click="checkLogin">确定</div>' +
					 '<div class="layui-btn layui-btn-normal cancel" @click="close">取消</div>' +
					 '</div>' +
					 '</div>' +
					 '</div>',
	props:['popDisplay'],
	data:function(){
		return {
			oldPassword:'',
			password:'',
			rePassword:''
		}
	},
	methods:{
		close:function( ){
			this.oldPassword='';
			this.password='';
			this.rePassword='';
			this.$emit('closepw','none');
		},
		//先确认原密码是否正确
		checkLogin:function(){
			var _this = this;
			$.ajax( {
				type: "POST",
				url: urlPrefix+"/user/login.do",
				data:{phone:getStorage("phone"), password:this.oldPassword },
				success: function( data ) {
					console.log( data);
					if( data.user ) {
						_this.changePassword();
					}
				},
				error: function( data ) {
					console.log( data.responseJSON );
					if (data.responseJSON.errCode == 1001) {
						myAlert({type:2,msg:'原密码不正确'});
					}
				}
			} );
		},
		changePassword:function(){
			var _this = this;
			if(this.password==='' || this.rePassword==='' || this.password!==this.rePassword){
				myAlert({type:2,msg:'两次输入的密码不一致'});
				return;
			}
			if(!/^[0-9a-zA-Z]{6,20}$/.test(this.password)){
				myAlert({type:2,msg:'请输入6-20位数字或字母组成的密码'});
				return;
			}
			$.ajax( {
				type: "POST",
				url: urlPrefix+"/user/changepassword.do",
				data:{userId:getStorage("userId"), password:_this.password},
				success: function( data )
				{
					console.log( data);
					if( data.change == 1 ) {
						_this.close();
						toggleDialog( {text:'修改成功'} );
					}
				},
				error: function( data ) {
					console.log( data.responseJSON );
					myAlert({type:2,msg:data.responseJSON.message});
				}
			} );
		}
	}
});
//分页
var mPage = Vue.extend({
	template:'<div class="m-page" v-show="item.total!==0">' +
						 '<button class="iconfont firstPageBtn" @click="changePage(1)" :disabled="item.currentPage===1">&#xe601;</button>' +
						 '<button class="iconfont prePageBtn" @click="changePage(item.currentPage-1)" :disabled="item.currentPage===1">&#xe64f;</button>' +
						 '<div class="somePages">' +
					 		'<span v-for="page in showPages" :class="{active:item.currentPage==page}" @click="changePage(page)">{{page}}</span></div>' +
						 '<button class="iconfont nextPageBtn" @click="changePage(item.currentPage+1)" :disabled="item.currentPage===item.totalPage">&#xe645;</button>' +
						 '<button class="iconfont lastPageBtn" @click="changePage(item.totalPage)" :disabled="item.currentPage===item.totalPage">&#xe600;</button>' +
						 '<div class="totalNum">共{{item.total}}条数据</div>' +
						 '<input type="number" v-model="inputPage" class="pageInput" placeholder="">/<div class="totalPage">{{item.totalPage}}页</div>' +
						 '<button class="skipBtn" @click="changePage(inputPage)" :disabled="inputPage>item.totalPage || inputPage<1">跳转</button>' +
					 '</div>',
	props:['item'],
	data:function(){
		return {
			inputPage:''
		}
	},
	computed:{
		showPages:function ()
		{
			var cur = this.item.currentPage;
			var tol =this.item.totalPage;
			var arr=[];
			if(tol<=3){
				for(var i=1;i<=tol;i++){
					arr.push(i);
				}
			}else{
				if(cur<tol && cur>1){
					arr = [cur-1,cur,cur+1];
				}else if(cur===tol){
					arr = [cur-2,cur-1,cur];
				}else if(cur===1){
					arr = [1,2,3];
				}
			}
			return arr;
		}
	},
	methods:{
		changePage:function(page){
			this.$emit('changepage',page);
		}
	},
	mounted:function( ){
		console.log(this.item);
	}
});

//退出登录
function layOut()
{
	deleteStorage(['isAdmin','userId','phone','nickname','myKeyword','myTypeId',
		'publicKeyword','publicTypeId','customerListPage','publicCstomerListPage','userListPage']);
	setTimeout("location.href = urlPrefix+'/user/logout.do';",100);
}

//mixins(public component options(like data,methods and so on))
var myMixin = {
	data:{
		popDisplay:'none'//修改密码弹窗的隐藏出现
	},
	methods: {
		//出现或隐藏密码框
		changePW:function(data){
			this.popDisplay=data;
		}
	}
}
