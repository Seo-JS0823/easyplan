const USER_MODAL = {
	Header: (title) => (parent) => {
		parent.child('div', header => {
			header.className('user-modal-header')
				.child('h1', h1 => h1.innerText(title))
		})
	},
	
	Control: (prop) => (parent) => {
		parent.child('div', ctrl => {
			ctrl.className('user-modal-control')
			.child('button', btn => {
				btn.id(prop.id)
				.innerText(prop.text)
				.on(prop.event, prop.key)
			})
			
			if(prop.user) {
				ctrl.child('a', a => {
					a.attribute('href', '#')
					.innerText('아이디 및 비밀번호 찾기')
				})				
			}
		})
	},
	
	LoginInput: (prop = {}) => (parent) => {
		parent.child('div', input => {
			input.className('user-modal-input')
				.child('div', group => {
					group.className('in-group')
					.child('label', label => { 
						label.attribute('for', prop.id[0])
						.innerText(prop.text[0])
					})
					.child('input', input => {
						input.attribute('type', 'text')
						.id(prop.id[0])
						.attribute('placeholder', prop.placeholder[0])
					})
				})
				.child('div', group => {
					group.className('in-group')
					.child('label', label => {
						label.attribute('for', prop.id[1])
						.innerText(prop.text[1])
					})
					.child('input', input => {
						input.attribute('type', 'password')
						.id(prop.id[1])
						.attribute('placeholder', prop.placeholder[1])
					})
				})
		})
	},
	
	SingupInput: (prop = {}) => (parent) => {
		parent.child('div', input => {
			input.className('user-modal-input')
			
			for(let i = 0; i < prop.sequnce.length; i++) {
				const key = prop.sequnce[i];
				const item = prop[key];
				
				input.child('div', group => {
					group.className('in-group')
					.child('p', label => {
						label.innerText(item.text)
					})
					.child('input', input => {
						input.attribute('type', item.type)
						.id(item.id)
						.attribute('placeholder', item.placeholder)
					})
				})	
			}
			
			input.child('div', group => {
				group.className('in-group')
				.child('p', label => {
					label.innerText('성별')
				})
				.child('div', group => {
					group.className('in-group radio')
					.child('div', radio => {
						radio.className('radio-box')
						.child('input', input => {
							input.attribute('type', 'radio')
							.id('signup-gender-MALE')
							.attribute('value', 'MALE')
							.attribute('name', 'gender')
						})
						.child('label', label => {
							label.innerText('남자').attribute('for', 'signup-gender-MALE')
						})
					})
					.child('div', radio => {
						radio.className('radio-box')
						.child('input', input => {
							input.attribute('type', 'radio')
							.id('signup-gender-FEMALE')
							.attribute('value', 'FEMALE')
							.attribute('name', 'gender')
						})
						.child('label', label => {
							label.innerText('여자').attribute('for', 'signup-gender-FEMALE')
						})
					})
					.child('div', radio => {
						radio.className('radio-box')
						.child('input', input => {
							input.attribute('type', 'radio')
							.id('signup-gender-NONE')
							.attribute('value', 'NONE')
							.attribute('name', 'gender')
						})
						.child('label', label => {
							label.innerText('비공개').attribute('for', 'signup-gender-NONE')
						})
					})
				})
			})
		})
	}
}

function loginModalComponent() {
	return UI.createFragment()
	.append(
		UI.createDocument('div', parent => {
			parent.className('user-modal')
			.use(USER_MODAL.Header('Easy Plan login'))
			.use(USER_MODAL.LoginInput({
				id: [ 'login-email', 'login-password' ],
				text: [ '아이디', '비밀번호' ],
				placeholder: [ '사용자 이메일', '사용자 비밀번호' ],
			}))
			.use(USER_MODAL.Control({
				id: 'login-run', event: 'click', key: 'LOGIN', text: '로그인', user: true
			}))
		})
	)
	.on('LOGIN', () => {
		alert('로그인')
	})
	.build();
}

function signupModalComponent() {
	return UI.createFragment()
	.append(
		UI.createDocument('div', parent => {
			parent.className('user-modal')
			.use(USER_MODAL.Header('Easy Plan signup'))
			.use(USER_MODAL.SingupInput({
				sequnce: ['email', 'password', 'nickname'],
				email: {
					id: 'signup-email',
					text: '사용자 아이디',
					type: 'text',
					placeholder: 'email@example.com'
				},
				password: {
					id: 'signup-password',
					text: '사용자 비밀번호',
					type: 'password',
					placeholder: '영문 소문자, 숫자, 특수문자를 포함한 9자리 이상'
				},
				nickname: {
					id: 'signup-nickname',
					text: '사용자 닉네임',
					type: 'text',
					placeholder: '2 ~ 10자의 영문 또는 한글'
				}
			}))
			.use(USER_MODAL.Control({
				id: 'signup-run', event: 'click', key: 'SIGNUP', text: '회원가입', user: false
			}))
		})
	)
	.on('SIGNUP', () => {
		alert('회원가입')
	})
	.build();
}