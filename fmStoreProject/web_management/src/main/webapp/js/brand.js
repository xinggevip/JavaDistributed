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
            _this = this;
            //here you can do custom state update
            this.page = page;
            axios.get('/brand/findPage.do',{params:{page:page,rows:this.pageSize}})
                .then(function (response) {
                    //取服务端响应的结果
                    _this.brandList = response.data.rows;
                    _this.total = response.data.total;
                    console.log(response);
                }).catch(function (reason) {
                console.log(reason);
            })



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
