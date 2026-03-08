$.id('login').on('click', () => {
	const modal = $.selector('.user-modal-overlay');
	
	$.toggle(modal, 'close');
	
	modal.replaceChildren(loginModalComponent())
});

$.id('signup').on('click', () => {
	const modal = $.selector('.user-modal-overlay');
	
	$.toggle(modal, 'close');
	
	modal.replaceChildren(signupModalComponent());
})

$.id('modal-overlay').on('click', (e) => {
	const modal = e.target;
	
	$.toggle(modal, 'close');
});