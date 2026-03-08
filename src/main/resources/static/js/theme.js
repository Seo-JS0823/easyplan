const savedTheme = localStorage.getItem('theme') || 'light';
document.documentElement.setAttribute('data-theme', savedTheme);

const tag = document.getElementById('theme');
tag.innerHTML = savedTheme === 'dark' ? darkModeHTML() : lightModeHTML();

document.getElementById('theme').addEventListener('click', (e) => {
	const theme = document.documentElement.getAttribute('data-theme');
	const nextTheme = theme === 'dark' ? 'light' : 'dark';
	
	if(nextTheme === 'dark') {
		tag.innerHTML = darkModeHTML();
	} else if(nextTheme === 'light') {
		tag.innerHTML = lightModeHTML();
	}
	
	document.documentElement.setAttribute('data-theme', nextTheme);
	localStorage.setItem('theme', nextTheme);
})

function lightModeHTML() {
	const html = `
	<svg class="yellow-icon" xmlns="http://www.w3.org/2000/svg" id="Layer_1" data-name="Layer 1" viewBox="0 0 24 24" width="20" height="20">
		<path d="M12,17c-2.76,0-5-2.24-5-5s2.24-5,5-5,5,2.24,5,5-2.24,5-5,5Zm0-9c-2.21,0-4,1.79-4,4s1.79,4,4,4,4-1.79,4-4-1.79-4-4-4Zm.5-3.5V.5c0-.28-.22-.5-.5-.5s-.5,.22-.5,.5V4.5c0,.28,.22,.5,.5,.5s.5-.22,.5-.5Zm0,19v-4c0-.28-.22-.5-.5-.5s-.5,.22-.5,.5v4c0,.28,.22,.5,.5,.5s.5-.22,.5-.5ZM5,12c0-.28-.22-.5-.5-.5H.5c-.28,0-.5,.22-.5,.5s.22,.5,.5,.5H4.5c.28,0,.5-.22,.5-.5Zm19,0c0-.28-.22-.5-.5-.5h-4c-.28,0-.5,.22-.5,.5s.22,.5,.5,.5h4c.28,0,.5-.22,.5-.5Zm-6.15-5.15l3-3c.2-.2,.2-.51,0-.71s-.51-.2-.71,0l-3,3c-.2,.2-.2,.51,0,.71,.1,.1,.23,.15,.35,.15s.26-.05,.35-.15ZM3.85,20.85l3-3c.2-.2,.2-.51,0-.71s-.51-.2-.71,0l-3,3c-.2,.2-.2,.51,0,.71,.1,.1,.23,.15,.35,.15s.26-.05,.35-.15ZM6.85,6.85c.2-.2,.2-.51,0-.71L3.85,3.15c-.2-.2-.51-.2-.71,0s-.2,.51,0,.71l3,3c.1,.1,.23,.15,.35,.15s.26-.05,.35-.15Zm14,14c.2-.2,.2-.51,0-.71l-3-3c-.2-.2-.51-.2-.71,0s-.2,.51,0,.71l3,3c.1,.1,.23,.15,.35,.15s.26-.05,.35-.15Z"/>
	</svg>
	<span>Light</span>
	`;
	
	return html;
}

function darkModeHTML() {
	const html = `
	<svg class="white-icon" xmlns:th="http://www.w3.org/2000/svg" id="Layer_1" data-name="Layer 1" viewBox="0 0 24 24" width="20" height="20">
		<path d="M15,24a12.021,12.021,0,0,1-8.914-3.966,11.9,11.9,0,0,1-3.02-9.309A12.122,12.122,0,0,1,13.085.152a13.061,13.061,0,0,1,5.031.205,2.5,2.5,0,0,1,1.108,4.226c-4.56,4.166-4.164,10.644.807,14.41a2.5,2.5,0,0,1-.7,4.32A13.894,13.894,0,0,1,15,24Zm.076-22a10.793,10.793,0,0,0-1.677.127,10.093,10.093,0,0,0-8.344,8.8A9.927,9.927,0,0,0,7.572,18.7,10.476,10.476,0,0,0,18.664,21.43a.5.5,0,0,0,.139-.857c-5.929-4.478-6.4-12.486-.948-17.449a.459.459,0,0,0,.128-.466.49.49,0,0,0-.356-.361A10.657,10.657,0,0,0,15.076,2Z"/>
	</svg>
	<span>Dark</span>
	`;
	
	return html;
}