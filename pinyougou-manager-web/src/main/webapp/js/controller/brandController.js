var app=new Vue({
    el:"#app",
    data:{
        list:[],
        entity:{},
        ids:[],
        pages:15,//总页数
        pageNo:1//当前页码
    },
    methods:{
        searchList:function (curPage) {
            console.log("AAA")
            axios.post('/brand/findPage.shtml?pageNo='+curPage).then(function (response) {
                //获取数据
                app.list=response.data.list;

                //当前页
                app.pageNo=curPage;
                //总页数
                app.pages=response.data.pages;
            });
        },
        //查询所有品牌列表
        findAll:function () {
            console.log(brand);
            axios.get('/brand/findAll.shtml').then(function (response) {
                console.log(response);
                //注意：this 在axios中就不再是 vue实例了。
                app.list=response.data;

            }).catch(function (error) {

            })
        },
    },

    //钩子函数
    created:function () {
        this.searchList(1);
    },


});
//不在生命周期的函数
function del() {
    //用原生的JS
   console.log(app.ids)
    axios.post("/brand/delBrand.shtml",app.ids).then(function (res) {
        if (res.data.success) {
            app.searchList(1);
        }else{
            alert("删除失败")
        }
    })

}