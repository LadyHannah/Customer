var vm = new Vue({
	el:'#app',
	mixins:[myMixin],
	data:{
		mHide:false,//审核历史
		loadingFlag:false,
		
		id:getUrlParm('id'),
		shopName:'',
		customerName:'',
		phone:'',
		typeId:'',
		product:'',
		content:'',
		contactList:[],
		typeList:[]
	},
	watch: {
		"phone":function(){
			this.duplicate();
		}
	},
	methods:{
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
						setTimeout( 'layui.form.render();', 100 );
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
		//手机号查重
		duplicate: function(){
			var _this = this;
			$.ajax( {
				type: "GET",
				url: urlPrefix + "/sys/duplicate.do",
				data: {phone:_this.phone},
				success: function( data ){
					if( data.current ) {
						myAlert({type:2, msg:'此客户已在'+data.current+'库里'});
					}
				},
				error: function( data ){
					console.log( data );
				}
			} );
		},
		//保存
		save:function(state){
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
			data.shopName = _this.shopName;
			data.customerName = _this.customerName;
			data.phone = _this.phone;
			data.typeId = _this.typeId;
			data.product = _this.product;
			data.content = _this.content;
			$.ajax( {
				type: "POST",
				url: urlPrefix + "/sys/save.do",
				data:data,
				success: function( data ){
					console.log( data );
					if( data.save == 1) {
						_this.loadingFlag=false;
						layer.msg("保存成功");
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

