EASY.select('#wrapper-toggle').on('click', (e) => {
	const wrapper = $.selector('.wrapper');
	
	wrapper.replaceChildren(signupHTML)
	
	if(e.target.getAttribute('data-state') === 'Login') {
		e.target.innerText = '회원가입';
		e.target.setAttribute('data-state', 'Signup');
	} else if(e.target.getAttribute('data-state') === 'Signup') {
		e.target.innerText = '로그인';
		e.target.setAttribute('data-state', 'Login');
	}
	
	console.log(e.target);
})

EASY.selectAll('.gender-option').onEach('click', (e) => {
	const currentOption = $.selector('.gender-option.checked');
	if(currentOption) {
		currentOption.classList.remove('checked');
	}
	$.toggle(e.target, 'checked');
})

EASY.select('#signup-run').on('click', async () => {
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