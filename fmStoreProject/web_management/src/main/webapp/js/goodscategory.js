new Vue({
    el: "#app",
    data:{
        categoryList:[],
        grade:1, /*当前级别*/
        gradeEntity1:{}, /*面包屑导航1*/
        gradeEntity2:{} /*面包屑导航2*/
    },
    methods: {
        selectCateByParentId: function (id) {
            var _this = this;
            axios.post("/itemCat/findByParentId.do?parentId="+id)
                .then(function (response) {
                    //取服务端响应的结果
                    _this.categoryList =response.data;
                    console.log(response);
                }).catch(function (reason) {
                console.log(reason);
            })
        },
        setGrade:function(grade){
            this.grade = grade;
        },
        nextGrade:function (item) {
            if (this.grade ==1){
                this.gradeEntity1 = {};
                this.gradeEntity2 = {};
            }
            if (this.grade == 2){
                this.gradeEntity1 = item;
                this.gradeEntity2 = {};
            }
            if (this.grade == 3){
                this.gradeEntity2 = item;
            }
            this.selectCateByParentId(item.id);
        }
    },
    created: function () {
        this.selectCateByParentId(0);
    }
});