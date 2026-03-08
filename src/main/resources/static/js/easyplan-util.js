function csrfToken() {
	if(document.cookie) {
		const value = `; ${document.cookie}`;
		const parts = value.split(`; XSRF-TOKEN=`);
		
		if(parts.length === 2) {
			return parts.pop().split(';').shift();
		}
	}
	return '';
}

function debounce(func, timeout = 500) {
	let timer;
	return (...args) => {
		clearTimeout(timer);
		timer = setTimeout(() => { func.apply(this, args); }, timeout);
	}
}

function timeFormat(date, time = false) {
	const year = date.getFullYear();
	const month = String(date.getMonth() + 1).padStart(2, '0');
	const day = String(date.getDate()).padStart(2, '0');
	
	if(time) {
		const hours = String(date.getHours()).padStart(2, '0');
		const minutes = String(date.getMinutes()).padStart(2, '0');
		const seconds = String(date.getSeconds()).padStart(2, '0');
		
		return `${year}. ${month}. ${day} ${hours} : ${minutes} : ${seconds}`;
	}
	
	return `${year}. ${month}. ${day}`;
}

const $ = {
	id: (id) => {
		let tag = document.getElementById(id);
		return {
			tag,
			
			on(event, handler) {
				tag.addEventListener(event, handler);
				return this;
			},
			
			build() {
				return tag;
			}
		}
	},
	
	selector: (name) => {
		let tag = document.querySelector(name);
		return tag;
	},
	
	toggle: (tag, className) => {
		if(tag instanceof HTMLElement) {
			tag.classList.toggle(className);
		}
	}
}

const UI = {
	createFragment() {
		const frag = document.createDocumentFragment();
		const handlers = {};
		
		return {
			append(builder) { frag.appendChild(builder.build()); return this; },
			on(key, handler) { handlers[key] = handler; return this; },
			build() {
				const root = document.createElement('div');
				root.style.display = 'contents';
				root.appendChild(frag);
				
				const eventTypes = new Set();
				
				root.querySelectorAll('[data-event]').forEach(el => eventTypes.add(el.dataset.event));
				
				eventTypes.forEach(type => {
					root.addEventListener(type, (e) => {
						const target = e.target.closest(`[data-event="${type}"]`);
						if(!target) return;
						
						const fn = handlers[target.dataset.key];
						if(fn) fn(e);
					});
				});
				
				return root;
			} // build
		}		// return
	},		// createFragment
	
	createDocument(name, setupFn = null) {
		const el = document.createElement(name);
		const builder = {
			el,
			id(v) { el.id = v; return this; },
			className(v) { el.className = v; return this; },
			innerText(v) { el.innerText = v; return this; },
			innerHTML(v) { el.innerHTML = v; return this; },
			attribute(p, v)  { el.setAttribute(p, v); return this; },
			on(event, key) { el.dataset.event = event; el.dataset.key = key; return this; },
			
			child(name, childSetupFn = null) {
				const childBuilder = UI.createDocument(name, childSetupFn);
				el.appendChild(childBuilder.el);
				return this;
			},
			
			use(componentFn) {
				if(componentFn) componentFn(this);
				return this;
			},
			
			build() { return el; }
		}
		
		if(setupFn) setupFn(builder);
		return builder;
	}
}

class FETCH {
	constructor(url) {
		this.url = url;
		this.options = {
			method: 'GET',
			headers: {
				'Content-Type': 'application/json',
				'X-XSRF-TOKEN': csrfToken()
			}
		}
	}
	
	post() { this.options.method = 'POST'; return this; }
	
	put() { this.options.method = 'PUT'; return this; }
	
	patch() { this.options.method = 'PATCH'; return this; }
	
	delete() { this.options.method = 'DELETE'; return this; }
	
	credentials() { this.options['credentials'] = 'include'; return this; }
	
	addHeader(prop, value) {
		this.options.headers[prop] = value;
		return this;
	}
	
	body(json) {
		if(!json) {
			console.error(`FETCH: DEBUG: (${json}) Parameter Not Found`)
			return {};
		}
		this.options['body'] = JSON.stringify(json);
		return this;
	}
	
	async send() {
		let res = await fetch(this.url, this.options);
		
		const data = await res.json();
		
		return data;
	}
}


































