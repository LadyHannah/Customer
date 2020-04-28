//alert弹窗事件
/**
 * sendOption-{
		 	"type":1,					-1:confirm样式；2：alert样式
			"msg":"确定吗？",		-显示信息
			"yes":"确定",			-确定按钮的字样
			"cancel":"取消"		-取消按钮的字样
			"yesParm":{}			-yesFun执行时需要的参数
			"cancelParm":{}		-cancelFun执行时需要的参数
 		}
 yesFun：点击确定按钮后执行的函数名
 cancelFun:点击取消按钮后执行的函数名
 * */
function myAlert(sendOption,yesFun,cancelFun){
	var defaults = {
		"type":1,
		"msg":"确定吗？",
		"yes":"确定",
		"cancel":"取消"
	};
	var option = $.extend(defaults, sendOption);
	//console.log(option);
	var btns = '';
	if(option.type===1){//confirm
		btns = '<div class="layui-btn layui-btn-normal yes">'+option.yes+'</div> ' +
					 '<div class="layui-btn layui-btn-primary cancel">'+option.cancel+'</div> ';
	}else{//alert
		btns =  '<div class="layui-btn layui-btn-normal yes" >'+option.yes+'</div> ';
	}

	$("body").append('<div class="myAlert"> ' +
									 '<div class="alertBody"> ' +
									 '<div class="text">'+option.msg+'</div> ' +
									 '<div class="btnArea"> ' +
									 btns +
									 '</div> ' +
									 '</div> ' +
									 '</div>');

	$(".myAlert").on("click",".cancel",function(){
		cancel();
		if(cancelFun){
			cancelFun(option.cancelParm);
		}
	});
	$(".myAlert").on("click",".yes",function(){
		cancel();
		if(yesFun){
			yesFun(option.yesParm);
		}
	});
	var cancel = function(){
		$(".myAlert").remove();
	}

}

//毫秒转化为yyyy-MM-dd HH:mm:ss(可指定转化形式)
var format = function( time, format )
{
	var t = new Date( time );
	var tf = function( i )
	{
		return (i < 10 ? '0' : '') + i
	};
	return format.replace( /yyyy|MM|dd|HH|mm|ss/g, function( a )
	{
		switch( a )
		{
			case 'yyyy':
				return tf( t.getFullYear() );
				break;
			case 'MM':
				return tf( t.getMonth() + 1 );
				break;
			case 'mm':
				return tf( t.getMinutes() );
				break;
			case 'dd':
				return tf( t.getDate() );
				break;
			case 'HH':
				return tf( t.getHours() );
				break;
			case 'ss':
				return tf( t.getSeconds() );
				break;
		}
	} )
};
//获取url传参
function getUrlParm( name )
{
	var reg = new RegExp( "(^|&)" + name + "=([^&]*)(&|$)" );
	var r = window.location.search.substr( 1 ).match( reg );
	if( r != null )return decodeURI( r[ 2 ] );
	return null;
}
//单页面后退
function reverseSlide( parm1, parm2 )
{
	$( parm1 ).animate( { left: '30rem' }, 300 ).hide( 100 );
	$( parm2 ).show();
}
//单页面前进
function pageSlide( parm1, parm2 )
{
	$( parm1 ).hide();
	$( parm2 ).show().animate( { left: '0' }, 300 );
}
//H5-localStorage
//存储
function setStorage( name, value )
{
	if( !window.localStorage )
	{
		console.log( "浏览器不支持localStorage" );
	}
	else
	{
		localStorage.setItem( name, value );
	}
}
//删除
function deleteStorage(list){
	for(var i=0;i<list.length;i++){
		localStorage.removeItem(list[i]);
	}
}
//获取
function getStorage( name )
{
	//console.log( localStorage );
	var val = localStorage.getItem( name );
	return val;
}
//替换历史记录里的当前url；parmArr-需要添加的参数数组
function replaceState( parmArr )
{
	var hash = "";
	if(location.href.split( "#" ).length!=1){
		hash ="#"+location.href.split( "#" )[ 1 ];
	}else{

	}
	var beforeHash = location.href.split( "#" )[ 0 ];
	var currentHref = beforeHash.split( "?" )[ 0 ];
	var searchStr = "";
	$.each( parmArr, function( j, parm )
	{//遍历参数数组
		for( var prop in parm )
		{//取每个对象里面的键值对
			if( j == 0 )
			{
				searchStr += "?" + prop + "=" + parm[ prop ];
			}
			else
			{
				searchStr += "&" + prop + "=" + parm[ prop ];
			}
		}
	} );
	history.replaceState( null, document.title, currentHref + searchStr +hash );
}
//插入一条历史url
function pushHistory() {
	var state = {title: "title",url: "#"};
	window.history.pushState(state, "title", "#");
}
//提示框-出现一段时间后消失/一直存在/有关闭按钮
/*
* option:{text:'',time:1500,closeIcon:false}
* 	time:0-一直存在，不消失
* */
function toggleDialog( option )
{
	var defaultOption = {
		text:'',
		time:1500,
		closeIcon:false
	};
	option = $.extend(defaultOption,option);
	$(".dialogCon").remove();
	var icon='';
	if(option.closeIcon){
		icon='<i class="closeIcon" onclick="$(this).parent(\'.dialogCon\').remove();">×</i>';
	}
	var dialogCon = '<div class="text-center dialogCon">' + option.text + icon+'</div>';
	$( "body" ).append( dialogCon );
	var _this = $(".dialogCon");
	var screenW = document.body.clientWidth;
	var dialogConW = _this.width();
	_this.css({"left":(screenW-dialogConW)/2});
	if(option.time===0){
		_this.fadeIn( 500 );
	}else{
		var delayTime = option.time? option.time:1500;
		_this.fadeIn( 500 ).delay( delayTime ).fadeOut( 300 );
	}
}
/*remove url of alert/confirm*/
var wAlert = window.alert;
window.alert = function( message )
{
	try
	{
		var iframe = document.createElement( "IFRAME" );
		iframe.style.display = "none";
		iframe.setAttribute( "src", 'data:text/plain,' );
		document.documentElement.appendChild( iframe );
		var alertFrame = window.frames[ 0 ];
		var iwindow = alertFrame.window;
		if( iwindow == undefined )
		{
			iwindow = alertFrame.contentWindow;
		}
		iwindow.alert( message );
		iframe.parentNode.removeChild( iframe );
	}
	catch( exc )
	{
		return wAlert( message );
	}
};

var wConfirm = window.confirm;
window.confirm = function( message )
{
	try
	{
		var iframe = document.createElement( "IFRAME" );
		iframe.style.display = "none";
		iframe.setAttribute( "src", 'data:text/plain,' );
		document.documentElement.appendChild( iframe );
		var alertFrame = window.frames[ 0 ];
		var iwindow = alertFrame.window;
		if( iwindow == undefined )
		{
			iwindow = alertFrame.contentWindow;
		}
		var result = iwindow.confirm( message );
		iframe.parentNode.removeChild( iframe );
		return result;
	}
	catch( exc )
	{
		return wConfirm( message );
	}
};


/*分页插件*/
(function(){
	var defaults = {
		currentPage:1,
		totalPage:5,
		callBack:function( page ){}
	};

	$.fn.mPage = function(sendOption) {
		//在这里面,this指的是用jQuery选中的元素
		//example :$('a'),则this=$('a')
		var option = $.extend(defaults, sendOption);
		return this.each(function() {
			//对每个元素进行操作
			var _this = $( this );
			var currentPage = parseInt(option.currentPage);
			var totalPage = option.totalPage;
			var preHide='',nextHide='';
			if(option.currentPage==1){//第一页时，隐藏上一页按钮
				preHide = 'm-hide';
			}
			if(option.currentPage==option.totalPage){//最后一页时，隐藏下一页按钮
				nextHide = 'm-hide';
			}
			_this.html('<button class="layui-btn layui-btn-primary prePageBtn '+preHide+'"></button> ' +
								 '<div><span class="currentPage">'+currentPage+'</span>/<span class="totalPage">'+totalPage+'</span></div> ' +
								 '<button class="layui-btn layui-btn-primary nextPageBtn '+nextHide+'"></button> ' +
								 '<input type="text" class="layui-input pageInput"> ' +
								 '<button class="layui-btn layui-btn-normal skipBtn">跳转</button>');
			_this.off( "click" );//解绑事件，防止多次执行
			_this.on( "click", ".prePageBtn", function(){
				option.callBack( currentPage - 1 );
			} );
			_this.on( "click", ".nextPageBtn", function(){
				option.callBack( currentPage + 1 );
			} );
			_this.on( "click", ".skipBtn", function(){
				var p = parseInt( $( ".pageInput" ).val() );
				console.log(p);
				if(p>totalPage || p <= 0 || !p ){//!p-没有输入值时，返回NaN,转为boolean-false
					toggleDialog({text:'请输入正确的页码'});
				}else{
					option.callBack( p );
				}
			} );
		})
	}
})();

/*load( 1 );
function load( i )
{
	console.log( 'i:' + i );
	分页插件用法
	$( "#m-page" ).mPage( {
		currentPage: i, totalPage: 5, callBack: function( page ){
			console.log( 'page:' + page );
			load( page );
		}
	} )
}*/

//获取地址前缀
var urlPrefix=getUrlPrefix();
function getUrlPrefix(){
	var curWwwPath = window.document.location.href;
	var pathName =  window.document.location.pathname;
	var pos = curWwwPath.indexOf(pathName);
	var localhostPaht = curWwwPath.substring(0, pos);
	var projectName = pathName.substring(0,pathName.substr(1).indexOf('/') + 1);
	return (localhostPaht + projectName);
}




