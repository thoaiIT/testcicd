'use strict'
let vm;
$(function() {
vm = new Vue({
    el: '#wrap',
    data: {
        user: {
            pwd: '',
            newPwd: '',
            checkPwd: ''
        },
        warn: {
            pwd: '',
            newPwd: '',
            checkPwd: ''
        }
    },
    created() {},
    mounted() {},
    methods: {
        fnSave() {
            //console.log('@@ methods - fnSave');
            const me = this;
            if (!me.fnSaveValidation()) return;

            me.$commonAxios({
                url: '/lo/02/01',
                type: 'POST'
            }, Object.assign({}, me.user), function(response) {
                //console.log('success', arguments, this);
                if (response.success === true) {
                    me.$commonAlert('비밀번호 재설정이 완료되었습니다.\n변경된 비밀번호로 로그인해주세요.', function() {
                        location.href = '/lo/01/02';  // logout
                    });
                } else {
                    if (response.message === 'INCLUDE_USERID') {
                        me.warn.newPwd = '비밀번호에 아이디가 포함되어 있습니다.';
                        me.$refs.newPwd.focus();
                    } else if (response.message === 'INVALID_PASSWORD') {
                        me.warn.pwd = '현재 비밀번호와 일치하지 않습니다.';
                        me.$refs.pwd.focus();
                    } else
                        me.$commonAlert('오류가 발생하였습니다.');  // never ...
                }
            });
        },
        fnSaveValidation() {
            const me = this;
            if (!me.user.pwd) {
                me.warn.pwd = '현재 비밀번호를 입력해주세요.';
                me.$refs.pwd.focus();
                return false;
            }
            if (!me.user.newPwd || me.user.newPwd.length < 8) {
                me.warn.newPwd = '비밀번호를 최소 8자리 이상 입력해주세요.';
                me.$refs.newPwd.focus();
                return false;
            }
            if (!me.user.newPwd.match(/^(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$/)) {
                me.warn.newPwd = '영문, 숫자, 특수문자를 조합하여 8자리 이상 입력해주세요.';
                me.$refs.newPwd.focus();
                return false;
            }
            if (/(\w)\1\1/.test(me.user.newPwd) || !fnChkConsChar(me.user.newPwd)) {
                me.warn.newPwd = '연속된 영문, 숫자, 문자는 사용 할 수 없습니다.';
                me.$refs.newPwd.focus();
                return false;
            }
            if (me.user.pwd === me.user.newPwd) {
                me.warn.newPwd = '입력하신 새 비밀번호가 기존 비밀번호와 동일합니다.';
                me.$refs.newPwd.focus();
                return false;
            }
            if (me.user.newPwd !== me.user.checkPwd) {
                me.warn.checkPwd = '비밀번호가 일치하지 않습니다.';
                me.$refs.checkPwd.focus();
                return false;
            }
            return true;
        }
    }
});
});