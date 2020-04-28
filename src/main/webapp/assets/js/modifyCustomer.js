var vm = new Vue({
	el:'#app',
	mixins:[myMixin],
	data:{
		loadingFlag:false,
		
		isAdmin: getStorage("isAdmin"),
		
		id:getUrlParm('id'),
		shopName:'',
		customerName:'',
		phone:'',
		typeId:'',
		product:'',
		isPublic:'',
		content:'',
		contactList:[],
		typeList:[]
	},
	watch:{
		
	},
	methods:{
		//获取客户详情
		getCustomer: function(){
			var _this = this;
			$.ajax( {
				type: "GET",
				url: urlPrefix + "/sys/getcustomer.do",
				data:{id:_this.id},
				success: function( data ){
					console.log( data );
					if( data.customer ) {
						_this.shopName = data.customer.shopName;
						_this.customerName = data.customer.customerName;
						_this.phone = data.customer.phone;
						_this.typeId = data.customer.typeId;
						_this.product = data.customer.product;
						_this.isPublic = data.customer.isPublic;
						_this.contactList = data.customer.contactList;
						defaultVal();
						setTimeout( 'layui.form.render("select");', 100 );
					}
				},
				error: function( data ){
					console.log( data );
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
						_this.typeList = data.customerType;
						setTimeout( 'layui.form.render("select");', 100 );
						console.log(_this.typeList);
					}
				},
				error: function( data ){
					console.log( data );
				}
			} );
		},
		//跳转我的客户
		goToIndex:function(){
			location.href = urlPrefix+"/sys/toindex.do";
		},
		//跳转我的客户
		goToPublic:function(){
			location.href = urlPrefix+"/sys/topubliccustomer.do";
		},
		//修改
		modify:function(){
			var _this = this;
			_this.loadingFlag=true;
			// 字段校验
			if (_this.customerName.trim().length == 0) {
				layer.msg("请填写客户姓名");
				return false;
			} 
			if (_this.phone.trim().length == 0) {
				layer.msg("请填写联系电话");
				return false;
			} 
			if (!layui.form.config.verify.phone[0].test(_this.phone)) {
				layer.msg(layui.form.config.verify.phone[1]);
				return false;
			}
			if (_this.typeId == '') {
				layer.msg("请选择客户类型");
				return false;
			} 
			if (_this.typeId == 1 && _this.product.trim().length == 0) {
				layer.msg("请填写已购产品");
				return false;
			} 
			var data = {};
			data.id = _this.id;
			data.shopName = _this.shopName;
			data.customerName = _this.customerName;
			data.phone = _this.phone;
			data.typeId = _this.typeId;
			data.product = _this.product;
			data.content = _this.content;
			$.ajax( {
				type: "POST",
				url: urlPrefix + "/sys/modify.do",
				data:data,
				success: function( data ){
					console.log( data );
					if( data.modify == 1) {
						_this.loadingFlag=false;
						layer.msg("修改成功");
						_this.goToIndex();
					}
				},
				error: function( data ){
					console.log( data.responseJON );
					_this.loadingFlag=false;
					layer.msg(data.responseJSON.message);
				}
			} );
		},
		drop:function(){
			myAlert({msg:'确定将此客户扔入公海？'},this.dropCustomer);
		},
		dropCustomer:function() {
			var _this = this;
			_this.loadingFlag=true;
			$.ajax( {
				type: "POST",
				url: urlPrefix + "/sys/drop.do",
				data:{id:_this.id},
				success: function( data ){
					console.log( data );
					if( data.drop == 1) {
						_this.loadingFlag=false;
						layer.msg("已扔入公海");
						_this.goToIndex();
					}
				},
				error: function( data ){
					console.log( data.responseJON );
					_this.loadingFlag=false;
					layer.msg(data.responseJSON.message);
				}
			} );
		},
		getBack:function(){
			myAlert({msg:'确定取回此客户？'},this.getBackCustomer);
		},
		getBackCustomer:function() {
			var _this = this;
			_this.loadingFlag=true;
			$.ajax( {
				type: "POST",
				url: urlPrefix + "/sys/getback.do",
				data:{id:_this.id},
				success: function( data ){
					console.log( data );
					if( data.getback == 1) {
						_this.loadingFlag=false;
						layer.msg("取回成功");
						_this.goToPublic();
					}
				},
				error: function( data ){
					console.log( data.responseJON );
					_this.loadingFlag=false;
					layer.msg(data.responseJSON.message);
				}
			} );
		},
		//点击删除
		deleted:function(){
			myAlert({msg:'确定删除此客户所有信息？'},this.deleteCustomer);
		},
		deleteCustomer:function() {
			var _this = this;
			_this.loadingFlag=true;
			$.ajax( {
				type: "POST",
				url: urlPrefix + "/sys/delete.do",
				data:{id:_this.id},
				success: function( data ){
					console.log( data );
					if( data.deleted == 1) {
						_this.loadingFlag=false;
						layer.msg("删除成功");
						_this.goToIndex();
					}
				},
				error: function( data ){
					console.log( data.responseJON );
					_this.loadingFlag=false;
					layer.msg(data.responseJSON.message);
				}
			} );
		},
	},
	created:function(){
		this.getCustomerTypeList();
		this.getCustomer();
	}
});

(function(){
	var form=layui.form;
	var $ = layui.jquery;

	//监听客户类型的选择
	form.on('select(customerType)', function(data){
		vm.typeId = data.value; //得到被选中的值
		console.log(vm.typeId);
	});
	
	form.render();
})();

//给select赋值
function defaultVal()
{
	layui.form.val('customerForm',{
		"customerType": vm.typeId
	})
}
