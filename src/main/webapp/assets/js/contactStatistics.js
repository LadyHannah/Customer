var vm = new Vue({
	el:'#app',
	mixins:[myMixin],
	data:{
		pageName:'联系量统计',//当前所在导航项的index

		keyword:'',
		contactList:[],
		pageData:{currentPage:1,totalPage:0,total:0},
	},
	methods:{
		//获取用户列表
		getContactList:function(page){
			var _this=this;
			$.ajax( {
				type: "GET",
				url: urlPrefix+"/data/getcontactstatistics.do",
				data: {
					keyword:this.keyword,
					currentPage:page,
					pageSize:10
				},
				success: function( data ) {
					console.log( data );
					if( data.contactList ) {
						_this.contactList = data.contactList;
						_this.pageData = data;
					}
				},
				error: function( data ) {
					console.log( data.responseJSON );
					layer.msg(data.responseJSON.message);
				}
			} );
		},
		//分页组件绑定事件
		changePage:function(page){
			this.getContactList(page);
		},
		keySearch:function (e) {
			if(e.keyCode === 13){
				this.getContactList(1);
			}
		}
	},
	mounted:function(){
		this.getContactList(1);
	},
	components:{'m-page':mPage}
});

(function(  ){
	var form=layui.form;
})();
