new Vue({
    el:"#app",
    data:{
        specList:[],
        spec:{
            specName:''
        },
        page: 1,  //显示的是哪一页 当前角标
        pageSize: 10, //每一页显示的数据条数
        total: 0, //记录总数
        maxPage:9, // 最大页码数
        searchSpec:{
            specName:''
        }
    },
    created(){
        this.pageHandler(1);
    },
    methods:{

        // pageNum发生变化
        pageHandler(page) {
            // alert(this.spec.specName);
            this.page = page;
            let _this = this;
            this.page = page;
            axios.post('/spec/search.do?page=' + page + '&rows=' + this.pageSize,this.searchSpec)
                .then(function (response) {
                    //取服务端响应的结果
                    _this.specList = response.data.rows;
                    _this.total = response.data.total;
                    console.log(response.data);
                }).catch(function (reason) {
                console.log(reason);
            })
        },
    }
})
