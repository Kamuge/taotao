var brand=new Vue({
    el:"#brand",
    data:{
        list:[],
        entity:{},
        pages:15,//总页数
        pageNo:1//当前页码
    },
    methods:{
        searchList:function (curPage) {
            axios.post('/brand/findPage.shtml?pageNo='+curPage).then(function (response) {
                //获取数据
                brand.list=response.data.list;

                //当前页
                brand.pageNo=curPage;
                //总页数
                brand.pages=response.data.pages;
            });
        },

        //查询所有品牌列表
        findAll:function () {
            console.log(brand);
            axios.get('/brand/findAll.shtml').then(function (response) {
                console.log(response);
                //注意：this 在axios中就不再是 vue实例了。
                brand.list=response.data;

            }).catch(function (error) {

            })
        }
    },
    //钩子函数
    created:function () {
        this.searchList(1);
    }

})