$.id('logout').on('click', async () => {
	const res = await new FETCH('/api/auth/logout').post().credentials().send();
	
	if(res.success == true) {
		window.location.href = '/';
	}
})