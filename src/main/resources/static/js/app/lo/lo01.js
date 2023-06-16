'use strict'
let vm, vmModal;
$(function() {
vm = new Vue({
    el: '#wrap',
    data: {
        username: 'admin',  // FIXME ...
        password: '1234'   // FIXME ...
    },
    created() {},
    mounted() {},
    methods: {
        fnLogin() {
            const me = this;
            if (!me.fnLoginValidation()) return;

            let form = new FormData();
            form.append('username', me.username);
            form.append('password', me.password);

            me.$commonAxios({
                url: '/lo/01',
                type: 'POST'
            }, form, function(response) {
                //console.log('login success', arguments, this);
                if (response.success === true) {
                    if (response.message === 'FIRST_LOGIN_TEMPORARY_USER')
                        me.$commonAlert('대교 ALL IN ONE 이용을 위한\n본인인증이 필요합니다.\n[확인] 클릭 시 인증이 진행됩니다.', function() {
                            location.href = '/lo/04';
                        });
                    else if (response.message === 'TEMPORARY_PASSWORD_USER')
                        me.$commonAlert('임시 비밀번호로 로그인하셨습니다.\n본인인증이 필요합니다.\n[확인] 클릭 시 인증이 진행됩니다.', function() {
                            location.href = '/lo/04';
                        });
                    else if (response.message === 'PERIOD_OVER_LAST_PASSWORD')
                        vmModal.fnShow();
                    else
                        location.href = '/';
                }
                else {
                    if (response.message === 'MAX_COUNTS_WRONG_PASSWORD')
                        me.$commonAlert('로그인 5회 실패로 로그인이 불가합니다.\n관리자에게 문의해주세요.');
                    else if (response.message === 'INVALID_USERNAME_PASSWORD')
                        me.$commonAlert('아이디 또는 비밀번호가 일치하지 않습니다.');
                    else if (response.message === 'USER_STATUS_ABSENCE')
                        me.$commonAlert('휴직 상태의 사용자입니다.\n로그인 하시려면 관리자에게 문의해주세요.');
                    else if (response.message === 'USER_STATUS_RETIRED')
                        me.$commonAlert('퇴사 상태의 사용자입니다.\n로그인 하시려면 관리자에게 문의해주세요.');
                    else
                        me.$commonAlert('아이디 또는 비밀번호가 일치하지 않습니다.');
                }
            });
        },
        fnLoginValidation() {
            const me = this;
            if (!me.username) {
                me.$commonAlert('아이디를 입력해 주세요.', function() {
                    me.$find('input[name="username"]').focus();
                });
                return false;
            }
            if (!me.password) {
                me.$commonAlert('비밀번호를 입력해 주세요.', function() {
                    me.$find('input[name="password"]').focus();
                });
                return false;
            }
            return true;
        }
    }
});

vmModal = new Vue({
    el: '#notiChgPw',
    data: {
        bsModal: undefined
    },
    created() {},
    mounted() {
        //console.log('@@ mounted');
        this.bsModal = new bootstrap.Modal(this.$el, {});
    },
    methods: {
        fnShow() {
            this.bsModal.show();
        },
        fnClose() {
            //console.log('@@ methods - fnClose()');
            location.href = '/';
        },
        fnAfter() {
            //console.log('@@ methods - fnAfter()');
            const me = this;
            me.$commonAxios({ url: '/lo/01/03', type: 'POST' }, {}, function(data) {
                //console.log('success', arguments, this);
                if (data === true)
                    location.href = '/';
                else
                    me.$commonAlert('오류가 발생하였습니다.');  // never ...
            });
        },
        fnChgPw() {
            //console.log('@@ methods - fnChgPw()');
            location.href = '/lo/02';
        }
    }
});
});