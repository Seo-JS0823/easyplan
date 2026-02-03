const signupHTML = $.createFragment()
	.append(
		$.createDocument('div')
			.className('wrapper-header')
			.child('h2')
				.innerText('Easy Plan sign up').up()
			.child('p')
				.innerText('쉽고 간편한 가계부를 이용하세요.').up()
	)
	.append(
		$.createDocument('div')
			.className('wrapper-group')
			.child('label')
				.attribute('for', 'signup-email')
				.innerText('이메일').up()
			.child('input')
				.attribute('type', 'text')
				.attribute('placeholder', 'email@example.com')
				.id('signup-email').up()
	)
	.append(
		$.createDocument('div')
			.className('wrapper-group')
			.child('label')
				.attribute('for', 'signup-nickname')
				.innerText('닉네임').up()
			.child('input')
				.attribute('type', 'text')
				.attribute('placeholder', 'Nickname')
				.id('signup-nickname').up()
	)
	.append(
		$.createDocument('div')
			.className('wrapper-group')
			.child('label')
				.attribute('for', 'signup-password')
				.innerText('비밀번호').up()
			.child('input')
				.attribute('type', 'password')
				.attribute('placeholder', 'Password')
				.id('signup-password').up()
	)
	.append(
		$.createDocument('div')
			.className('wrapper-group')
			.child('label')
				.innerText('성별')
				.up()
			.child('div')
			.className('gender-options')
				.child('button')
					.className('gender-option checked')
					.attribute('data-gender', 'MALE')
					.innerText('남자')
					.on('click', 'GENDER')
					.up()
				.child('button')
					.className('gender-option')
					.attribute('data-gender', 'FEMALE')
					.innerText('여자')
					.on('click', 'GENDER')
					.up()
				.child('button')
					.className('gender-option')
					.attribute('data-gender', 'OTHER')
					.innerText('비공개')
					.on('click', 'GENDER')
					.up()
				.up()
	)
	.append(
		$.createDocument('button')
			.innerText('회원가입')
			.className('wrapper-btn')
			.on('click', 'SIGNUP')
			.up()
	)
	.on('GENDER', (e) => {
		const clicked = e.target.closest('.gender-option');
		if(!clicked) return;
		
		const current = $.selector('.gender-option.checked');
		if(current) current.classList.remove('checked');
		
		$.toggle(e.target, 'checked');
	})
	.on('SIGNUP', async () => {
		const email = $.id('signup-email').value;
		const nickname = $.id('signup-nickname').value;
		const password = $.id('signup-password').value;
		const gender = $.selector('.gender-option.checked').getAttribute('data-gender');
		
		const user = {
			email: email,
			nickname: nickname,
			password: password,
			gender: gender
		}
		
		const res = await new EasyFetch('/api/auth/signup')
		.authPostCookieBody(user)
		.send();
		
		console.log(res);
	})
	.build()