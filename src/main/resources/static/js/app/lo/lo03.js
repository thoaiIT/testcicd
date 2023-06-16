'use strict'
let vm;
$(function() {
vm = new Vue({
    el: '#wrap',
    data: {
        user: {
            id: '',
            name: '',
            roleName: '',
            deptName: '',
            birth: '',
            mobile: '',
            email: {
                id: '',
                provider: ''
            },
        },
        emailProvider: ''
    },
    created() {
        console.log('created()', arguments, this);
        const me = this;
        me.$commonAxios({ url: '/lo/03/01', type: 'GET' }, {}, function(data) {
            //console.log('success', arguments, this);
            me.user = data;
        });
    },
    mounted() {},
    methods: {
        fnChgEmailProvider(e) {
            console.log('@@ methods - fnChgEmailProvider');
            this.user.email.provider = e.target.value;
        },
        fnSave() {
            console.log('@@ methods - fnSave');
            const me = this;
            if (!me.fnSaveValidation()) return;

            const user = {
                deptName: me.user.deptName,
                email: me.user.email
            };

            me.$commonConfirm('내 정보를 수정 하시겠습니까?', function() {
                me.$commonAxios({ url: '/lo/03/02', type: 'POST' }, user, function(data) {
                    //console.log('success', arguments, this);
                    if (data === true)
                        location.reload();
                    else
                        me.$commonAlert('오류가 발생하였습니다.');  // never ...
                });
            });
        },
        fnSaveValidation() {
            const me = this;
            if (!me.user.deptName) {
                me.$commonAlert('소속부서를 입력해주세요.', function() {
                    me.$refs.deptName.focus();
                });
                return false;
            }
            if (!me.user.email.id) {
                me.$commonAlert('이메일을 정확하게 입력해주세요.', function() {
                    me.$refs.emailId.focus();
                });
                return false;
            }
            if (!me.user.email.provider) {
                me.$commonAlert('이메일을 정확하게 입력해주세요.', function() {
                    me.$refs.emailProvider.focus();
                });
                return false;
            }
            return true;
        }
    }
});
});