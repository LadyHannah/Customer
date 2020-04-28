var vm;
vm = new Vue( {
	el: '#app',
	mixins:[myMixin],
	data: {
		pageName: '公海客户',//当前所在导航项的index
		
		isAdmin: getStorage("isAdmin"),
		keyword: '',
		customerTypeId:'',
		customerTypeList:[],
		customerList: [],
		pageData:{currentPage:1,totalPage:0,total:0},
	},
	methods: {
		//获取患者记录列表
		getCustomerList: function( currentPage ){
			var _this = this;
			var data =  {
				typeId: _this.customerTypeId,
				keyword:_this.keyword,
				currentPage:currentPage,
				pageSize:10
			};
			$.ajax( {
				type: "GET",
				url: urlPrefix + "/sys/getpubliccustomerlist.do",
				data:data,
				success: function( data ){
					console.log( data );
					if( data.customer ) {
						_this.customerList = data.customer;
						_this.pageData = data;
						if (getStorage("publicTypeId")) {
							_this.customerTypeId = getStorage("publicTypeId");
							defaultVal();
							setTimeout( 'layui.form.render("select");', 100 );
						}
					}
				},
				error: function( data ){
					console.log( data.responseJON );
					layer.msg(data.responseJSON.message);
				}
			} );
		},
		//获取客户类型
		getCustomerTypeList: function(){
			var _this = this;
			$.ajax( {
				type: "GET",
				url: urlPrefix + "/sys/type/getcustomertypelist.do",
				success: function( data ){
					console.log( data );
					if( data.customerType ) {
						_this.customerTypeList = data.customerType;
						setTimeout( 'layui.form.render();', 100 );
					}
				},
				error: function( data ){
					console.log( data.responseJON );
					layer.msg(data.responseJSON.message);
				}
			} );
		},
		//跳转详情页
		goToModify:function(item){
			//存储当前页码
			setStorage( 'publicCustomerListPage', this.pageData.currentPage );
			location.href = urlPrefix+'/sys/tomodifycustomer.do?id='+item.id;
		},
		//分页组件绑定事件
		changePage:function(page){
			this.getCustomerList(page);
		},
		// 回车键触发查询
		keySearch:function (e) {
			if(e.keyCode === 13){
				this.getCustomerList(1);
			}
		}
	},
	watch:{
		'customerTypeId':function(val){
			this.getCustomerList(1);	
		},
		'keyword':function(val){
			setStorage("publicKeyword", val);	
		}
	},
	created: function(){
		this.getCustomerTypeList();		
		this.keyword = getStorage("publicKeyword")?getStorage("publicKeyword"):"";
		var currentPage = getStorage("publicCustomerListPage")?getStorage("publicCustomerListPage"):1;
		this.pageData.currentPage=currentPage;
		this.getCustomerList( currentPage );		
	},
	components: {'m-page':mPage }
} );

(function(  ){
	var form=layui.form;
	var $ = layui.jquery;

	//监听客户类型的选择
	form.on('select(customerTypeSelect)', function(data){
		vm.customerTypeId = data.value; //得到被选中的值
		setStorage("publicTypeId", data.value);
	});
	
})();

function defaultVal() {
	layui.form.val('publicCustomerForm',{
		"customerTypeSelect": vm.customerTypeId
	})
}