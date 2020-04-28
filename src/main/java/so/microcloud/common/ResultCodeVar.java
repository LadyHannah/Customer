package so.microcloud.common;

/**
 * 返回编码
 * @author caonw
 *
 */
public enum ResultCodeVar {

	RESULTCODE_000("未知异常",000),
	
	RESULTCODE_200("请求成功",200),
	
	RESULTCODE_400("参数异常",400),
	
	RESULTCODE_401("用户未登入",401),
	
	RESULTCODE_403("用户无该接口权限",403),
	
	RESULTCODE_404("请求资源不存在",404),
	
	RESULTCODE_500("服务器异常",500),
	
	RESULTCODE_507("签名验证失败",507),
	
	RESULTCODE_509("已在别的地方登录",509),
	
	RESULTCODE_1001("手机号或密码错误",1001),
	RESULTCODE_1002("此手机号已存在",1002),
	RESULTCODE_1003("登录超时",1003),
	RESULTCODE_1004("当前库容已满",1004),
	
	//通用模块
	RESULTCODE_10001("系统异常",10001),
	RESULTCODE_10011("参数不正确",10011),
	RESULTCODE_10013("未查询到相关数据",10013),
	RESULTCODE_10014("文件上传失败",10014),
	RESULTCODE_10015("新增患者失败",10015),
	RESULTCODE_10016("该患者已存在",10016),
	RESULTCODE_10017("该患者不存在",10017),
	RESULTCODE_10018("文件解析失败",10018),
	RESULTCODE_10019("该手机号码已存在",10019),
	RESULTCODE_10020("该角色名已存在",10020),
	RESULTCODE_10021("角色已被分配不能删除",10021),
	RESULTCODE_10022("两次密码不一致",10022),
	
	RESULTCODE_20102("保存失败",20102),
	RESULTCODE_20103("信息未填写完整",20103),
	RESULTCODE_20104("删除失败",20104),
	RESULTCODE_20105("原密码错误",20105),
	RESULTCODE_20106("申请失败",20106),
	RESULTCODE_20107("撤销申请失败",20107),
	RESULTCODE_20108("审核失败",20108),
	RESULTCODE_20109("该患者存在未审核的申请，暂不可发起新的申请",20109),
	;
	
    private String name ;
    
    private int index ;
     
    private ResultCodeVar( String name , int index ){
        this.name = name ;
        this.index = index ;
    }
     
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }

}
