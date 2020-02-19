new Vue({
    el:"#app",
    data:{
        brandList:[],
        brand:{},
        page: 1,  //显示的是哪一页 当前角标
        pageSize: 10, //每一页显示的数据条数
        total: 0, //记录总数
        maxPage:9,
        selectedID:[],
        searchObj:{}
    },
    created(){
        this.pageHandler(1);
    },
    methods:{
        // pageNum发生变化
        pageHandler(page) {
            this.page = page;
            var _this = this;
            // 发送请求
            axios.post("/brand/findAllBrands.do").then(function (response) {
                // 响应成功执行
                console.log(response);
            }).catch(function (reason) {
                // 响应失败执行
            });
        },
        // 保存和更新
        save(){

        },
        // 根据id查找
        findById(id){

        },
        // 删除选中多个
        deleteSelection(event,id){

        },
        // 删除选中单个
        deleteBrand(){

        }


    }
})
