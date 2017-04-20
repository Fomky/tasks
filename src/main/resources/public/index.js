/**
 * Created by cxx15 on 2017/4/13.
 */
var apps = new Vue({
    el: "#apps",
    data: {
        message: "测试弹窗了---",
        clist: [],
        chose: {},
        list: [],
        log_list: [],
        log_list_id:-1,
        edit : null,
        isEdit: false,
        box_show: false,
        name: null,
        a: {success: 'alert-success', warning: 'alert-warning', danger: 'alert-danger'},
        className: 'alert-success'
    },
    created: function () {
        this.findLogs(-1, 0)
    },
    methods: {
        selectTask: function (e) {
            var value = Number($(e.target).val());
            this.log_list_id = value;
            this.findLogs(value, 0)
        },
        addTaskClick: function () {
            this.isEdit = false;
            this.chose = {};
            this.name = null;
            $('#task-selects').val(-1)
        },
        loadList: function () {
            var that = this;
            $.post("task/list", {}, function (res) {
                that.list = res;
                setTimeout(function () {
                    $('[data-toggle="tooltip"]').tooltip()
                }, 200)
            }, 'json')
        },
        scheduler: function () {
            var that = this;
            $.post("task/scheduler", {}, function (res) {
                console.log(res)
            }, 'json')
        },
        loadCList: function () {
            var that = this;
            $.post("task/base", {}, function (res) {
                that.clist = res;
                //if(res.length>0)that.chose = res[0]
            })
        },
        findLogs: function (id, start) {
            var data = {id: id, start: start};
            var that = this;
            $.ajax("task/findLogs", {
                type: "POST",
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify(data),
                success: function (res) {
                    that.log_list = res;
                }
            })
        },
        refreshLogList:function () {
            this.findLogs(this.log_list_id,0)
        },
        alert: function (message, className) {
            if (message !== null && message !== undefined) {
                var that = this;
                that.message = message;
                if (className !== null && className !== undefined) {
                    that.className = className;
                }
                that.box_show = true;
                setTimeout(function () {
                    that.box_show = false;
                }, 1000)
            }
        },
        choseTask: function (e) {
            var index = $(e.target).val();
            var chose = index >= 0 ? this.clist.slice(index)[0] : {};
            this.chose = $.extend({},this.chose,chose);
            console.log(this.chose)
        },
        addTask: function () {
            $('#add_task').modal('toggle');
            var that = this;
            var data = this.chose;
            data.name = this.name;
            if (this.isEdit) {
                data.id = this.edit.id;
                $.ajax("task/edit", {
                    type: "POST",
                    dataType: "json",
                    contentType: "application/json",
                    data: JSON.stringify(data),
                    success: function (res) {
                        that.alert('更新任务成功', that.a.success);
                        that.loadList();
                    }
                })
            } else {
                $.ajax("task/add", {
                    type: "POST",
                    dataType: "json",
                    contentType: "application/json",
                    data: JSON.stringify(data),
                    success: function (res) {
                        if(res.status===200){
                            that.alert('添加任务成功', that.a.success);
                            that.loadList();
                        }else{
                            that.alert(res.message, that.a.danger);
                        }
                    }
                })
            }
        },
        editTask: function (e) {
            var that = this;
            var index = $(e.target).parents("tr").index();
            var data = that.list[index];
            this.edit = data.task;
            that.name = data.task.name;
            that.clist.forEach(function (obj, index) {
                if (obj.class_name === data.task.class_name) {
                    $('#task-selects').val(index);
                }
            });
            that.isEdit = true;
            that.chose = data.task;
            $('#add_task').modal('toggle')
        },
        deleteTask: function (e) {
            var index = $(e.target).parents("tr").index();
            var that = this;
            var data = this.list.slice(index)[0];
            $.ajax("task/delete", {
                type: "POST",
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify(data),
                success: function (res) {
                    that.alert('删除任务成功', that.a.success);
                    that.loadList();
                }
            })
        }
    }
});
apps.loadCList();
apps.loadList();
apps.scheduler();