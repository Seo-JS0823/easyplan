let accessToken;

function csrfToken() {
	if(document.cookie) {
		const cookieValue = `; ${document.cookie}`;
		const parts = cookieValue.split(`; XSRF-TOKEN=`);
		
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

const getId = (id) => document.getElementById(id);

const $ = {
	id: (id) => document.getElementById(id),
	selector: (name) => document.querySelector(name),
	selectorAll: (name) => document.querySelectorAll(name),
	on: (target, event, handler) => {
		if(target instanceof HTMLElement) {
			target.addEventListener(event, handler);
		}
	},
	toggle: (target, className) => {
		if(target instanceof HTMLElement) {
			target.classList.toggle(className);
		}
	},
	createFragment() {
		const frag = document.createDocumentFragment();
		const handlers = {};
		
		return {
			append(builder) {
				frag.appendChild(builder.build());
				return this;
			},
			
			appendRaw(node) {
				frag.appendChild(node);
				return this;
			},
			
			on(key, handler) {
				handlers[key] = handler;
				return this;
			},
			
			build() {
				const root = document.createElement('div');
				root.style.display = 'contents';
				root.appendChild(frag);
				
				root.addEventListener('click', (e) => {
					const target = e.target.closest('[data-event]');
					if(!target) return;
					
					if(target.dataset.event === 'click') {
						const fn = handlers[target.dataset.key];
						if (fn) fn(e);
					}
				})
				
				return root;
			}
		}
	},
	createDocument(name, parent = null) {
		const el = document.createElement(name);
		return {
			el,
			
			id(name) {
				el.id = name;
				return this;
			},
			
			className(name) {
				el.className = name;
				return this;
			},
			
			innerText(text) {
				el.innerText = text;
				return this;
			},
			
			attribute(prop, value) {
				el.setAttribute(prop, value);
				return this;
			},
			
			on(event, key) {
				el.dataset.event = event;
				el.dataset.key = key;
				return this;
			},
			
			child(name) {
				const childBuilder = $.createDocument(name, this);
				el.appendChild(childBuilder.el);
				return childBuilder;
			},
			
			up() {
				return parent || this;
			},
			
			build() {
				return el;
			}
		}
	}
}

const EASY = {
	select(name) {
		const el = document.querySelector(name);
		if(!el) {
			console.warn(`DEBUG: ${name} Not Found`);
			return;
		}
		return {
			el,
			// 이벤트 등록
			on(event, handler) {
				if(el) el.addEventListener(event, handler);
				return this;
			},
			// 여러 이벤트 등록
			onAny(events) {
				if(el) {
					for(const [event, handler] of Object.entires(events)) {
						el.addEventListener(event, handler)
					}
				}
				return this;
			},
			// CSS 제어
			css(prop, value) {
				if(el) el.style[prop] = value;
				return this;
			},
			// 텍스트 변경
			text(text) {
				if(el) el.innerText = text;
				return this;
			}
		}
	},
	selectAll(name) {
		const els = document.querySelectorAll(name);
		let length = 0;
		if(!els) {
			console.warn(`DEBUG: ${name} Not Found`);
			return;
		}
		length = els.length;
		return {
			els,
			onEach(event, handler) {
				for(let i = 0; i < length; i++) {
					els[i].addEventListener(event, handler);
				}
				return this;
			}
		}
	}
}

class EasyFetch {
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
	
	// 귀찮아
	authPostCookie() {
		this.post().auth().credentials();
		return this;
	}
	
	authPostCookieBody(json) {
		this.authPostCookie().body(json);
		return this;
	}
	
	auth() {
		if(!accessToken) return this;
		
		this.options.headers['Authorization'] = accessToken;
		return this;
	}
	
	addHeader(prop, value) {
		this.options.headers[prop] = value;
		return this;
	}
	
	body(json) {
		if(!json) {
			console.warn(`DEBUG: (${json}) Parameter Not Found`);
			return undefined;
		}
		this.options['body'] = JSON.stringify(json);
		return this;
	}
	
	async send() {
		let res = await fetch(this.url, this.options);
		return res.json();
	}
}