const MY_MODAL = {
	Header: (title) => (parent) => {
		parent.child('div', header => {
			header.className('header')
				.innerHTML(mypageProfileIcon())
				.child('h1', text => text.innerText(title))
		})
	},
	
	Content: (content = {}, control = {}) => (parent) => {
		parent.child('div', ctnt => {
			ctnt.className('content')
			
			ctnt.child('div', grp => {
				grp.className('content-group')
					.child('label', label => { label.innerText(content.sequnce[0].title) })
					.child('input', input => {
						input.attribute('type', content.sequnce[0].type)
						.id(content.sequnce[0].id)
						.on(content.sequnce[0].event, content.sequnce[0].key)
						if(content.sequnce[0].value) {
							input.attribute('value', content.sequnce[0].value);
						}
					})
			})
			
			for(let i = 1; i < content.sequnce.length; i++) {
				ctnt.child('div', grp => {
					grp.className('content-group')
						.child('label', label => { label.innerText(content.sequnce[i].title) })
						.child('input', input => {
							input.attribute('type', content.sequnce[i].type)
							.id(content.sequnce[i].id)
							.on(content.sequnce[i].event, content.sequnce[i].key)
							if(content.sequnce[i].value) {
								input.attribute('value', content.sequnce[i].value);
							}
						})
				})
			}
			
			ctnt.child('div', grp => {
				grp.className('content-group')
					.child('button', btn => {
						btn.innerText(control.text)
						.id(control.id)
						.on(control.event, control.key)
					})
			})
		})
	}
}

function mypageProfileIcon() {
	const html = `
		<svg class="white-icon" xmlns:th="http://www.w3.org/2000/svg" id="Outline" viewBox="0 0 24 24" width="25" height="25">
			<path d="M19,8.424V7A7,7,0,0,0,5,7V8.424A5,5,0,0,0,2,13v6a5.006,5.006,0,0,0,5,5H17a5.006,5.006,0,0,0,5-5V13A5,5,0,0,0,19,8.424ZM7,7A5,5,0,0,1,17,7V8H7ZM20,19a3,3,0,0,1-3,3H7a3,3,0,0,1-3-3V13a3,3,0,0,1,3-3H17a3,3,0,0,1,3,3Z"/>
			<path d="M12,14a1,1,0,0,0-1,1v2a1,1,0,0,0,2,0V15A1,1,0,0,0,12,14Z"/>
		</svg>
	`;
	
	return html;
}

function mypageProfileNicknameMatchOpen() {
	const title = '닉네임 변경'
	
	const content = {
		sequnce: [
			{
				title: '현재 비밀번호',
				type: 'password',
				id: 'profile-password',
				event: 'keydown',
				key: 'PROFILE_PASSWORD_ENTER_RUN'
			}
		]
	}
	
	const control = {
		text: '확인',
		id: 'profile-password-match',
		event: 'click',
		key: 'PROFILE_PASSWORD_MATCH'
	}
	
	return UI.createFragment()
	.append(
		UI.createDocument('div', parent => {
			parent.className('mypage-modal')
			.use(MY_MODAL.Header(title))
			.use(MY_MODAL.Content(content, control))
		})
	)
	.on('PROFILE_PASSWORD_ENTER_RUN', async (e) => {
		if(e.key === 'Enter') {			
			const res = await passwordMatch();
			
			if(res.success === true) {
				const password = e.target.value;
				const modal = $.selector('.mypage-modal-overlay');
				
				modal.replaceChildren(profileNicknameModal(password));
			}
		}
	})
	.on('PROFILE_PASSWORD_MATCH', async () => {
		const res = await passwordMatch();
		if(res.success === true) {
			const password = $.id('profile-password').build().value; 
			
			const modal = $.selector('.mypage-modal-overlay');
			
			modal.replaceChildren(profileNicknameModal(password));
		}
	})
	.build()
}

function mypageProfilePasswordMatchOpen() {
	const title = '비밀번호 변경'
	
	const content = {
		sequnce: [
			{
				title: '현재 비밀번호',
				type: 'password',
				id: 'profile-password',
				event: 'keydown',
				key: 'PROFILE_PASSWORD_ENTER_RUN'
			}
		]
	}
	
	const control = {
		text: '확인',
		id: 'profile-password-match',
		event: 'click',
		key: 'PROFILE_PASSWORD_MATCH'
	}
	
	return UI.createFragment()
	.append(
		UI.createDocument('div', parent => {
			parent.className('mypage-modal')
			.use(MY_MODAL.Header(title))
			.use(MY_MODAL.Content(content, control))
		})
	)
	.on('PROFILE_PASSWORD_ENTER_RUN', async (e) => {
		if(e.key === 'Enter') {			
			const res = await passwordMatch();
			console.log(res);
			if(res.success === true) {
				const password = e.target.value;
				const modal = $.selector('.mypage-modal-overlay');
				
				modal.replaceChildren(profileUpdateModal(password));
			}
		}
	})
	.on('PROFILE_PASSWORD_MATCH', async () => {
		const res = await passwordMatch();
		if(res.success === true) {
			const password = $.id('profile-password').build().value; 
			
			const modal = $.selector('.mypage-modal-overlay');
			
			modal.replaceChildren(profileUpdateModal(password));
		}
	})
	.build() // 새로운 비밀번호 입력했을 때, passwordMatch 할 때 쓴 패스워드 값을 어떻게 들고와서 같이 실행해야 할 듯!
}

async function passwordMatch() {
	const user = {
		password: $.id('profile-password').build().value
	}
	
	const res = await new FETCH('/api/user/p-m').post().credentials().body(user).send();
	
	return res;
}

function profileNicknameModal(password) {
	const title = '닉네임 변경'
	
	const content = {
		sequnce: [
			{
				title: '새로운 닉네임',
				type: 'text',
				id: 'profile-nickname',
				event: 'keydown',
				key: 'PROFILE_NICKNAME'
			}
		]
	}
	
	const control = {
		text: '닉네임 변경',
		id: 'profile-update',
		event: 'click',
		key: 'PROFILE_UPDATE'
	}
	
	return UI.createFragment()
	.append(
		UI.createDocument('div', parent => {
			parent.className('mypage-modal')
			.use(MY_MODAL.Header(title))
			.use(MY_MODAL.Content(content, control))
		})
	)
	.on('PROFILE_NICKNAME', async (e) => {
		if(e.key === 'Enter') {
			passwordNicknameFetch(password);
		}
	})
	.on('PROFILE_UPDATE', async () => {
		passwordNicknameFetch(password);
	})
	.build()
}

function profileUpdateModal(password) {
	const title = '비밀번호 변경'
	
	const content = {
		sequnce: [
			{
				title: '새로운 비밀번호',
				type: 'password',
				id: 'profile-password',
				event: 'keydown',
				key: 'PROFILE_PASSWORD'
			}
		]
	}
	
	const control = {
		text: '회원정보 변경',
		id: 'profile-update',
		event: 'click',
		key: 'PROFILE_UPDATE'
	}
	
	return UI.createFragment()
	.append(
		UI.createDocument('div', parent => {
			parent.className('mypage-modal')
			.use(MY_MODAL.Header(title))
			.use(MY_MODAL.Content(content, control))
		})
	)
	.on('PROFILE_PASSWORD', async (e) => {
		if(e.key === 'Enter') {
			passwordUpdateFetch(password);
		}
	})
	.on('PROFILE_UPDATE', async () => {
		passwordUpdateFetch(password);
	})
	.build()
}

async function passwordUpdateFetch(password) {
	const user = {
		currentPassword: password,
		newPassword: $.id('profile-password').build().value
	}

	const res = await new FETCH('/api/user/update/p').patch().credentials().body(user).send();
	
	if(res.success === true) {
		alert(res.message);
		window.location.href = '/';
	}
}

async function passwordNicknameFetch(password) {
	const user = {
		currentPassword: password,
		newNickname: $.id('profile-nickname').build().value
	}

	const res = await new FETCH('/api/user/update/n').patch().credentials().body(user).send();
	
	if(res.success === true) {
		alert(res.message);
		window.location.href = '/my/profile';
	}
}