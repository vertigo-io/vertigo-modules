//#region \0rolldown/runtime.js
var e = Object.defineProperty, t = (t, n) => {
	let r = {};
	for (var i in t) e(r, i, {
		get: t[i],
		enumerable: !0
	});
	return n || e(r, Symbol.toStringTag, { value: "Module" }), r;
};
//#endregion
//#region node_modules/vue-router/dist/vue-router.mjs
window.Vue.getCurrentInstance;
var n = window.Vue.inject;
window.Vue.onUnmounted, window.Vue.onDeactivated, window.Vue.onActivated;
var r = window.Vue.computed, i = window.Vue.unref;
window.Vue.watchEffect;
var a = window.Vue.defineComponent, o = window.Vue.reactive, s = window.Vue.h, c = window.Vue.provide, l = window.Vue.ref, u = window.Vue.watch, d = window.Vue.shallowRef, f = window.Vue.shallowReactive, p = window.Vue.nextTick, m = typeof document < "u";
function h(e) {
	return typeof e == "object" || "displayName" in e || "props" in e || "__vccOpts" in e;
}
function g(e) {
	return e.__esModule || e[Symbol.toStringTag] === "Module" || e.default && h(e.default);
}
var _ = Object.assign;
function v(e, t) {
	let n = {};
	for (let r in t) {
		let i = t[r];
		n[r] = b(i) ? i.map(e) : e(i);
	}
	return n;
}
var y = () => {}, b = Array.isArray, x = /#/g, ee = /&/g, te = /\//g, S = /=/g, ne = /\?/g, C = /\+/g, w = /%5B/g, re = /%5D/g, ie = /%5E/g, T = /%60/g, E = /%7B/g, D = /%7C/g, ae = /%7D/g, O = /%20/g;
function oe(e) {
	return encodeURI("" + e).replace(D, "|").replace(w, "[").replace(re, "]");
}
function se(e) {
	return oe(e).replace(E, "{").replace(ae, "}").replace(ie, "^");
}
function ce(e) {
	return oe(e).replace(C, "%2B").replace(O, "+").replace(x, "%23").replace(ee, "%26").replace(T, "`").replace(E, "{").replace(ae, "}").replace(ie, "^");
}
function k(e) {
	return ce(e).replace(S, "%3D");
}
function le(e) {
	return oe(e).replace(x, "%23").replace(ne, "%3F");
}
function ue(e) {
	return e == null ? "" : le(e).replace(te, "%2F");
}
function A(e) {
	try {
		return decodeURIComponent("" + e);
	} catch {}
	return "" + e;
}
var de = /\/$/, fe = (e) => e.replace(de, "");
function pe(e, t, n = "/") {
	let r, i = {}, a = "", o = "", s = t.indexOf("#"), c = t.indexOf("?");
	return s < c && s >= 0 && (c = -1), c > -1 && (r = t.slice(0, c), a = t.slice(c + 1, s > -1 ? s : t.length), i = e(a)), s > -1 && (r ||= t.slice(0, s), o = t.slice(s, t.length)), r = be(r ?? t, n), {
		fullPath: r + (a && "?") + a + o,
		path: r,
		query: i,
		hash: A(o)
	};
}
function me(e, t) {
	let n = t.query ? e(t.query) : "";
	return t.path + (n && "?") + n + (t.hash || "");
}
function he(e, t) {
	return !t || !e.toLowerCase().startsWith(t.toLowerCase()) ? e : e.slice(t.length) || "/";
}
function ge(e, t, n) {
	let r = t.matched.length - 1, i = n.matched.length - 1;
	return r > -1 && r === i && j(t.matched[r], n.matched[i]) && _e(t.params, n.params) && e(t.query) === e(n.query) && t.hash === n.hash;
}
function j(e, t) {
	return (e.aliasOf || e) === (t.aliasOf || t);
}
function _e(e, t) {
	if (Object.keys(e).length !== Object.keys(t).length) return !1;
	for (let n in e) if (!ve(e[n], t[n])) return !1;
	return !0;
}
function ve(e, t) {
	return b(e) ? ye(e, t) : b(t) ? ye(t, e) : e === t;
}
function ye(e, t) {
	return b(t) ? e.length === t.length && e.every((e, n) => e === t[n]) : e.length === 1 && e[0] === t;
}
function be(e, t) {
	if (e.startsWith("/")) return e;
	if (!e) return t;
	let n = t.split("/"), r = e.split("/"), i = r[r.length - 1];
	(i === ".." || i === ".") && r.push("");
	let a = n.length - 1, o, s;
	for (o = 0; o < r.length; o++) if (s = r[o], s !== ".") if (s === "..") a > 1 && a--;
	else break;
	return n.slice(0, a).join("/") + "/" + r.slice(o).join("/");
}
var xe = {
	path: "/",
	name: void 0,
	params: {},
	query: {},
	hash: "",
	fullPath: "/",
	matched: [],
	meta: {},
	redirectedFrom: void 0
}, Se;
(function(e) {
	e.pop = "pop", e.push = "push";
})(Se ||= {});
var Ce;
(function(e) {
	e.back = "back", e.forward = "forward", e.unknown = "";
})(Ce ||= {});
function we(e) {
	if (!e) if (m) {
		let t = document.querySelector("base");
		e = t && t.getAttribute("href") || "/", e = e.replace(/^\w+:\/\/[^\/]+/, "");
	} else e = "/";
	return e[0] !== "/" && e[0] !== "#" && (e = "/" + e), fe(e);
}
var Te = /^[^#]+#/;
function Ee(e, t) {
	return e.replace(Te, "#") + t;
}
function De(e, t) {
	let n = document.documentElement.getBoundingClientRect(), r = e.getBoundingClientRect();
	return {
		behavior: t.behavior,
		left: r.left - n.left - (t.left || 0),
		top: r.top - n.top - (t.top || 0)
	};
}
var Oe = () => ({
	left: window.scrollX,
	top: window.scrollY
});
function ke(e) {
	let t;
	if ("el" in e) {
		let n = e.el, r = typeof n == "string" && n.startsWith("#"), i = typeof n == "string" ? r ? document.getElementById(n.slice(1)) : document.querySelector(n) : n;
		if (!i) return;
		t = De(i, e);
	} else t = e;
	"scrollBehavior" in document.documentElement.style ? window.scrollTo(t) : window.scrollTo(t.left == null ? window.scrollX : t.left, t.top == null ? window.scrollY : t.top);
}
function Ae(e, t) {
	return (history.state ? history.state.position - t : -1) + e;
}
var je = /* @__PURE__ */ new Map();
function Me(e, t) {
	je.set(e, t);
}
function Ne(e) {
	let t = je.get(e);
	return je.delete(e), t;
}
var Pe = () => location.protocol + "//" + location.host;
function Fe(e, t) {
	let { pathname: n, search: r, hash: i } = t, a = e.indexOf("#");
	if (a > -1) {
		let t = i.includes(e.slice(a)) ? e.slice(a).length : 1, n = i.slice(t);
		return n[0] !== "/" && (n = "/" + n), he(n, "");
	}
	return he(n, e) + r + i;
}
function Ie(e, t, n, r) {
	let i = [], a = [], o = null, s = ({ state: a }) => {
		let s = Fe(e, location), c = n.value, l = t.value, u = 0;
		if (a) {
			if (n.value = s, t.value = a, o && o === c) {
				o = null;
				return;
			}
			u = l ? a.position - l.position : 0;
		} else r(s);
		i.forEach((e) => {
			e(n.value, c, {
				delta: u,
				type: Se.pop,
				direction: u ? u > 0 ? Ce.forward : Ce.back : Ce.unknown
			});
		});
	};
	function c() {
		o = n.value;
	}
	function l(e) {
		i.push(e);
		let t = () => {
			let t = i.indexOf(e);
			t > -1 && i.splice(t, 1);
		};
		return a.push(t), t;
	}
	function u() {
		let { history: e } = window;
		e.state && e.replaceState(_({}, e.state, { scroll: Oe() }), "");
	}
	function d() {
		for (let e of a) e();
		a = [], window.removeEventListener("popstate", s), window.removeEventListener("beforeunload", u);
	}
	return window.addEventListener("popstate", s), window.addEventListener("beforeunload", u, { passive: !0 }), {
		pauseListeners: c,
		listen: l,
		destroy: d
	};
}
function Le(e, t, n, r = !1, i = !1) {
	return {
		back: e,
		current: t,
		forward: n,
		replaced: r,
		position: window.history.length,
		scroll: i ? Oe() : null
	};
}
function Re(e) {
	let { history: t, location: n } = window, r = { value: Fe(e, n) }, i = { value: t.state };
	i.value || a(r.value, {
		back: null,
		current: r.value,
		forward: null,
		position: t.length - 1,
		replaced: !0,
		scroll: null
	}, !0);
	function a(r, a, o) {
		let s = e.indexOf("#"), c = s > -1 ? (n.host && document.querySelector("base") ? e : e.slice(s)) + r : Pe() + e + r;
		try {
			t[o ? "replaceState" : "pushState"](a, "", c), i.value = a;
		} catch (e) {
			console.error(e), n[o ? "replace" : "assign"](c);
		}
	}
	function o(e, n) {
		a(e, _({}, t.state, Le(i.value.back, e, i.value.forward, !0), n, { position: i.value.position }), !0), r.value = e;
	}
	function s(e, n) {
		let o = _({}, i.value, t.state, {
			forward: e,
			scroll: Oe()
		});
		a(o.current, o, !0), a(e, _({}, Le(r.value, e, null), { position: o.position + 1 }, n), !1), r.value = e;
	}
	return {
		location: r,
		state: i,
		push: s,
		replace: o
	};
}
function ze(e) {
	e = we(e);
	let t = Re(e), n = Ie(e, t.state, t.location, t.replace);
	function r(e, t = !0) {
		t || n.pauseListeners(), history.go(e);
	}
	let i = _({
		location: "",
		base: e,
		go: r,
		createHref: Ee.bind(null, e)
	}, t, n);
	return Object.defineProperty(i, "location", {
		enumerable: !0,
		get: () => t.location.value
	}), Object.defineProperty(i, "state", {
		enumerable: !0,
		get: () => t.state.value
	}), i;
}
function Be(e) {
	return e = location.host ? e || location.pathname + location.search : "", e.includes("#") || (e += "#"), ze(e);
}
function Ve(e) {
	return typeof e == "string" || e && typeof e == "object";
}
function He(e) {
	return typeof e == "string" || typeof e == "symbol";
}
var Ue = Symbol(""), We;
(function(e) {
	e[e.aborted = 4] = "aborted", e[e.cancelled = 8] = "cancelled", e[e.duplicated = 16] = "duplicated";
})(We ||= {});
function Ge(e, t) {
	return _(/* @__PURE__ */ Error(), {
		type: e,
		[Ue]: !0
	}, t);
}
function Ke(e, t) {
	return e instanceof Error && Ue in e && (t == null || !!(e.type & t));
}
var qe = "[^/]+?", Je = {
	sensitive: !1,
	strict: !1,
	start: !0,
	end: !0
}, Ye = /[.+*?^${}()[\]/\\]/g;
function Xe(e, t) {
	let n = _({}, Je, t), r = [], i = n.start ? "^" : "", a = [];
	for (let t of e) {
		let e = t.length ? [] : [90];
		n.strict && !t.length && (i += "/");
		for (let r = 0; r < t.length; r++) {
			let o = t[r], s = 40 + (n.sensitive ? .25 : 0);
			if (o.type === 0) r || (i += "/"), i += o.value.replace(Ye, "\\$&"), s += 40;
			else if (o.type === 1) {
				let { value: e, repeatable: n, optional: c, regexp: l } = o;
				a.push({
					name: e,
					repeatable: n,
					optional: c
				});
				let u = l || qe;
				if (u !== qe) {
					s += 10;
					try {
						RegExp(`(${u})`);
					} catch (t) {
						throw Error(`Invalid custom RegExp for param "${e}" (${u}): ` + t.message);
					}
				}
				let d = n ? `((?:${u})(?:/(?:${u}))*)` : `(${u})`;
				r || (d = c && t.length < 2 ? `(?:/${d})` : "/" + d), c && (d += "?"), i += d, s += 20, c && (s += -8), n && (s += -20), u === ".*" && (s += -50);
			}
			e.push(s);
		}
		r.push(e);
	}
	if (n.strict && n.end) {
		let e = r.length - 1;
		r[e][r[e].length - 1] += .7000000000000001;
	}
	n.strict || (i += "/?"), n.end ? i += "$" : n.strict && !i.endsWith("/") && (i += "(?:/|$)");
	let o = new RegExp(i, n.sensitive ? "" : "i");
	function s(e) {
		let t = e.match(o), n = {};
		if (!t) return null;
		for (let e = 1; e < t.length; e++) {
			let r = t[e] || "", i = a[e - 1];
			n[i.name] = r && i.repeatable ? r.split("/") : r;
		}
		return n;
	}
	function c(t) {
		let n = "", r = !1;
		for (let i of e) {
			(!r || !n.endsWith("/")) && (n += "/"), r = !1;
			for (let e of i) if (e.type === 0) n += e.value;
			else if (e.type === 1) {
				let { value: a, repeatable: o, optional: s } = e, c = a in t ? t[a] : "";
				if (b(c) && !o) throw Error(`Provided param "${a}" is an array but it is not repeatable (* or + modifiers)`);
				let l = b(c) ? c.join("/") : c;
				if (!l) if (s) i.length < 2 && (n.endsWith("/") ? n = n.slice(0, -1) : r = !0);
				else throw Error(`Missing required param "${a}"`);
				n += l;
			}
		}
		return n || "/";
	}
	return {
		re: o,
		score: r,
		keys: a,
		parse: s,
		stringify: c
	};
}
function Ze(e, t) {
	let n = 0;
	for (; n < e.length && n < t.length;) {
		let r = t[n] - e[n];
		if (r) return r;
		n++;
	}
	return e.length < t.length ? e.length === 1 && e[0] === 80 ? -1 : 1 : e.length > t.length ? t.length === 1 && t[0] === 80 ? 1 : -1 : 0;
}
function Qe(e, t) {
	let n = 0, r = e.score, i = t.score;
	for (; n < r.length && n < i.length;) {
		let e = Ze(r[n], i[n]);
		if (e) return e;
		n++;
	}
	if (Math.abs(i.length - r.length) === 1) {
		if ($e(r)) return 1;
		if ($e(i)) return -1;
	}
	return i.length - r.length;
}
function $e(e) {
	let t = e[e.length - 1];
	return e.length > 0 && t[t.length - 1] < 0;
}
var et = {
	type: 0,
	value: ""
}, tt = /[a-zA-Z0-9_]/;
function nt(e) {
	if (!e) return [[]];
	if (e === "/") return [[et]];
	if (!e.startsWith("/")) throw Error(`Invalid path "${e}"`);
	function t(e) {
		throw Error(`ERR (${n})/"${l}": ${e}`);
	}
	let n = 0, r = n, i = [], a;
	function o() {
		a && i.push(a), a = [];
	}
	let s = 0, c, l = "", u = "";
	function d() {
		l &&= (n === 0 ? a.push({
			type: 0,
			value: l
		}) : n === 1 || n === 2 || n === 3 ? (a.length > 1 && (c === "*" || c === "+") && t(`A repeatable param (${l}) must be alone in its segment. eg: '/:ids+.`), a.push({
			type: 1,
			value: l,
			regexp: u,
			repeatable: c === "*" || c === "+",
			optional: c === "*" || c === "?"
		})) : t("Invalid state to consume buffer"), "");
	}
	function f() {
		l += c;
	}
	for (; s < e.length;) {
		if (c = e[s++], c === "\\" && n !== 2) {
			r = n, n = 4;
			continue;
		}
		switch (n) {
			case 0:
				c === "/" ? (l && d(), o()) : c === ":" ? (d(), n = 1) : f();
				break;
			case 4:
				f(), n = r;
				break;
			case 1:
				c === "(" ? n = 2 : tt.test(c) ? f() : (d(), n = 0, c !== "*" && c !== "?" && c !== "+" && s--);
				break;
			case 2:
				c === ")" ? u[u.length - 1] == "\\" ? u = u.slice(0, -1) + c : n = 3 : u += c;
				break;
			case 3:
				d(), n = 0, c !== "*" && c !== "?" && c !== "+" && s--, u = "";
				break;
			default:
				t("Unknown state");
				break;
		}
	}
	return n === 2 && t(`Unfinished custom RegExp for param "${l}"`), d(), o(), i;
}
function rt(e, t, n) {
	let r = _(Xe(nt(e.path), n), {
		record: e,
		parent: t,
		children: [],
		alias: []
	});
	return t && !r.record.aliasOf == !t.record.aliasOf && t.children.push(r), r;
}
function it(e, t) {
	let n = [], r = /* @__PURE__ */ new Map();
	t = ut({
		strict: !1,
		end: !0,
		sensitive: !1
	}, t);
	function i(e) {
		return r.get(e);
	}
	function a(e, n, r) {
		let i = !r, s = ot(e);
		s.aliasOf = r && r.record;
		let l = ut(t, e), u = [s];
		if ("alias" in e) {
			let t = typeof e.alias == "string" ? [e.alias] : e.alias;
			for (let e of t) u.push(ot(_({}, s, {
				components: r ? r.record.components : s.components,
				path: e,
				aliasOf: r ? r.record : s
			})));
		}
		let d, f;
		for (let t of u) {
			let { path: u } = t;
			if (n && u[0] !== "/") {
				let e = n.record.path, r = e[e.length - 1] === "/" ? "" : "/";
				t.path = n.record.path + (u && r + u);
			}
			if (d = rt(t, n, l), r ? r.alias.push(d) : (f ||= d, f !== d && f.alias.push(d), i && e.name && !ct(d) && o(e.name)), pt(d) && c(d), s.children) {
				let e = s.children;
				for (let t = 0; t < e.length; t++) a(e[t], d, r && r.children[t]);
			}
			r ||= d;
		}
		return f ? () => {
			o(f);
		} : y;
	}
	function o(e) {
		if (He(e)) {
			let t = r.get(e);
			t && (r.delete(e), n.splice(n.indexOf(t), 1), t.children.forEach(o), t.alias.forEach(o));
		} else {
			let t = n.indexOf(e);
			t > -1 && (n.splice(t, 1), e.record.name && r.delete(e.record.name), e.children.forEach(o), e.alias.forEach(o));
		}
	}
	function s() {
		return n;
	}
	function c(e) {
		let t = dt(e, n);
		n.splice(t, 0, e), e.record.name && !ct(e) && r.set(e.record.name, e);
	}
	function l(e, t) {
		let i, a = {}, o, s;
		if ("name" in e && e.name) {
			if (i = r.get(e.name), !i) throw Ge(1, { location: e });
			s = i.record.name, a = _(at(t.params, i.keys.filter((e) => !e.optional).concat(i.parent ? i.parent.keys.filter((e) => e.optional) : []).map((e) => e.name)), e.params && at(e.params, i.keys.map((e) => e.name))), o = i.stringify(a);
		} else if (e.path != null) o = e.path, i = n.find((e) => e.re.test(o)), i && (a = i.parse(o), s = i.record.name);
		else {
			if (i = t.name ? r.get(t.name) : n.find((e) => e.re.test(t.path)), !i) throw Ge(1, {
				location: e,
				currentLocation: t
			});
			s = i.record.name, a = _({}, t.params, e.params), o = i.stringify(a);
		}
		let c = [], l = i;
		for (; l;) c.unshift(l.record), l = l.parent;
		return {
			name: s,
			path: o,
			params: a,
			matched: c,
			meta: lt(c)
		};
	}
	e.forEach((e) => a(e));
	function u() {
		n.length = 0, r.clear();
	}
	return {
		addRoute: a,
		resolve: l,
		removeRoute: o,
		clearRoutes: u,
		getRoutes: s,
		getRecordMatcher: i
	};
}
function at(e, t) {
	let n = {};
	for (let r of t) r in e && (n[r] = e[r]);
	return n;
}
function ot(e) {
	let t = {
		path: e.path,
		redirect: e.redirect,
		name: e.name,
		meta: e.meta || {},
		aliasOf: e.aliasOf,
		beforeEnter: e.beforeEnter,
		props: st(e),
		children: e.children || [],
		instances: {},
		leaveGuards: /* @__PURE__ */ new Set(),
		updateGuards: /* @__PURE__ */ new Set(),
		enterCallbacks: {},
		components: "components" in e ? e.components || null : e.component && { default: e.component }
	};
	return Object.defineProperty(t, "mods", { value: {} }), t;
}
function st(e) {
	let t = {}, n = e.props || !1;
	if ("component" in e) t.default = n;
	else for (let r in e.components) t[r] = typeof n == "object" ? n[r] : n;
	return t;
}
function ct(e) {
	for (; e;) {
		if (e.record.aliasOf) return !0;
		e = e.parent;
	}
	return !1;
}
function lt(e) {
	return e.reduce((e, t) => _(e, t.meta), {});
}
function ut(e, t) {
	let n = {};
	for (let r in e) n[r] = r in t ? t[r] : e[r];
	return n;
}
function dt(e, t) {
	let n = 0, r = t.length;
	for (; n !== r;) {
		let i = n + r >> 1;
		Qe(e, t[i]) < 0 ? r = i : n = i + 1;
	}
	let i = ft(e);
	return i && (r = t.lastIndexOf(i, r - 1)), r;
}
function ft(e) {
	let t = e;
	for (; t = t.parent;) if (pt(t) && Qe(e, t) === 0) return t;
}
function pt({ record: e }) {
	return !!(e.name || e.components && Object.keys(e.components).length || e.redirect);
}
function mt(e) {
	let t = {};
	if (e === "" || e === "?") return t;
	let n = (e[0] === "?" ? e.slice(1) : e).split("&");
	for (let e = 0; e < n.length; ++e) {
		let r = n[e].replace(C, " "), i = r.indexOf("="), a = A(i < 0 ? r : r.slice(0, i)), o = i < 0 ? null : A(r.slice(i + 1));
		if (a in t) {
			let e = t[a];
			b(e) || (e = t[a] = [e]), e.push(o);
		} else t[a] = o;
	}
	return t;
}
function ht(e) {
	let t = "";
	for (let n in e) {
		let r = e[n];
		if (n = k(n), r == null) {
			r !== void 0 && (t += (t.length ? "&" : "") + n);
			continue;
		}
		(b(r) ? r.map((e) => e && ce(e)) : [r && ce(r)]).forEach((e) => {
			e !== void 0 && (t += (t.length ? "&" : "") + n, e != null && (t += "=" + e));
		});
	}
	return t;
}
function gt(e) {
	let t = {};
	for (let n in e) {
		let r = e[n];
		r !== void 0 && (t[n] = b(r) ? r.map((e) => e == null ? null : "" + e) : r == null ? r : "" + r);
	}
	return t;
}
var _t = Symbol(""), vt = Symbol(""), yt = Symbol(""), bt = Symbol(""), xt = Symbol("");
function St() {
	let e = [];
	function t(t) {
		return e.push(t), () => {
			let n = e.indexOf(t);
			n > -1 && e.splice(n, 1);
		};
	}
	function n() {
		e = [];
	}
	return {
		add: t,
		list: () => e.slice(),
		reset: n
	};
}
function Ct(e, t, n, r, i, a = (e) => e()) {
	let o = r && (r.enterCallbacks[i] = r.enterCallbacks[i] || []);
	return () => new Promise((s, c) => {
		let l = (e) => {
			e === !1 ? c(Ge(4, {
				from: n,
				to: t
			})) : e instanceof Error ? c(e) : Ve(e) ? c(Ge(2, {
				from: t,
				to: e
			})) : (o && r.enterCallbacks[i] === o && typeof e == "function" && o.push(e), s());
		}, u = a(() => e.call(r && r.instances[i], t, n, l)), d = Promise.resolve(u);
		e.length < 3 && (d = d.then(l)), d.catch((e) => c(e));
	});
}
function wt(e, t, n, r, i = (e) => e()) {
	let a = [];
	for (let o of e) for (let e in o.components) {
		let s = o.components[e];
		if (!(t !== "beforeRouteEnter" && !o.instances[e])) if (h(s)) {
			let c = (s.__vccOpts || s)[t];
			c && a.push(Ct(c, n, r, o, e, i));
		} else {
			let c = s();
			a.push(() => c.then((a) => {
				if (!a) throw Error(`Couldn't resolve component "${e}" at "${o.path}"`);
				let s = g(a) ? a.default : a;
				o.mods[e] = a, o.components[e] = s;
				let c = (s.__vccOpts || s)[t];
				return c && Ct(c, n, r, o, e, i)();
			}));
		}
	}
	return a;
}
function Tt(e) {
	let t = n(yt), a = n(bt), o = r(() => {
		let n = i(e.to);
		return t.resolve(n);
	}), s = r(() => {
		let { matched: e } = o.value, { length: t } = e, n = e[t - 1], r = a.matched;
		if (!n || !r.length) return -1;
		let i = r.findIndex(j.bind(null, n));
		if (i > -1) return i;
		let s = At(e[t - 2]);
		return t > 1 && At(n) === s && r[r.length - 1].path !== s ? r.findIndex(j.bind(null, e[t - 2])) : i;
	}), c = r(() => s.value > -1 && kt(a.params, o.value.params)), l = r(() => s.value > -1 && s.value === a.matched.length - 1 && _e(a.params, o.value.params));
	function u(n = {}) {
		if (Ot(n)) {
			let n = t[i(e.replace) ? "replace" : "push"](i(e.to)).catch(y);
			return e.viewTransition && typeof document < "u" && "startViewTransition" in document && document.startViewTransition(() => n), n;
		}
		return Promise.resolve();
	}
	return {
		route: o,
		href: r(() => o.value.href),
		isActive: c,
		isExactActive: l,
		navigate: u
	};
}
function Et(e) {
	return e.length === 1 ? e[0] : e;
}
var Dt = /* @__PURE__ */ a({
	name: "RouterLink",
	compatConfig: { MODE: 3 },
	props: {
		to: {
			type: [String, Object],
			required: !0
		},
		replace: Boolean,
		activeClass: String,
		exactActiveClass: String,
		custom: Boolean,
		ariaCurrentValue: {
			type: String,
			default: "page"
		},
		viewTransition: Boolean
	},
	useLink: Tt,
	setup(e, { slots: t }) {
		let i = o(Tt(e)), { options: a } = n(yt), c = r(() => ({
			[jt(e.activeClass, a.linkActiveClass, "router-link-active")]: i.isActive,
			[jt(e.exactActiveClass, a.linkExactActiveClass, "router-link-exact-active")]: i.isExactActive
		}));
		return () => {
			let n = t.default && Et(t.default(i));
			return e.custom ? n : s("a", {
				"aria-current": i.isExactActive ? e.ariaCurrentValue : null,
				href: i.href,
				onClick: i.navigate,
				class: c.value
			}, n);
		};
	}
});
function Ot(e) {
	if (!(e.metaKey || e.altKey || e.ctrlKey || e.shiftKey) && !e.defaultPrevented && !(e.button !== void 0 && e.button !== 0)) {
		if (e.currentTarget && e.currentTarget.getAttribute) {
			let t = e.currentTarget.getAttribute("target");
			if (/\b_blank\b/i.test(t)) return;
		}
		return e.preventDefault && e.preventDefault(), !0;
	}
}
function kt(e, t) {
	for (let n in t) {
		let r = t[n], i = e[n];
		if (typeof r == "string") {
			if (r !== i) return !1;
		} else if (!b(i) || i.length !== r.length || r.some((e, t) => e !== i[t])) return !1;
	}
	return !0;
}
function At(e) {
	return e ? e.aliasOf ? e.aliasOf.path : e.path : "";
}
var jt = (e, t, n) => e ?? t ?? n, Mt = /*#__PURE__*/ a({
	name: "RouterView",
	inheritAttrs: !1,
	props: {
		name: {
			type: String,
			default: "default"
		},
		route: Object
	},
	compatConfig: { MODE: 3 },
	setup(e, { attrs: t, slots: a }) {
		let o = n(xt), d = r(() => e.route || o.value), f = n(vt, 0), p = r(() => {
			let e = i(f), { matched: t } = d.value, n;
			for (; (n = t[e]) && !n.components;) e++;
			return e;
		}), m = r(() => d.value.matched[p.value]);
		c(vt, r(() => p.value + 1)), c(_t, m), c(xt, d);
		let h = l();
		return u(() => [
			h.value,
			m.value,
			e.name
		], ([e, t, n], [r, i, a]) => {
			t && (t.instances[n] = e, i && i !== t && e && e === r && (t.leaveGuards.size || (t.leaveGuards = i.leaveGuards), t.updateGuards.size || (t.updateGuards = i.updateGuards))), e && t && (!i || !j(t, i) || !r) && (t.enterCallbacks[n] || []).forEach((t) => t(e));
		}, { flush: "post" }), () => {
			let n = d.value, r = e.name, i = m.value, o = i && i.components[r];
			if (!o) return Nt(a.default, {
				Component: o,
				route: n
			});
			let c = i.props[r], l = s(o, _({}, c ? c === !0 ? n.params : typeof c == "function" ? c(n) : c : null, t, {
				onVnodeUnmounted: (e) => {
					e.component.isUnmounted && (i.instances[r] = null);
				},
				ref: h
			}));
			return Nt(a.default, {
				Component: l,
				route: n
			}) || l;
		};
	}
});
function Nt(e, t) {
	if (!e) return null;
	let n = e(t);
	return n.length === 1 ? n[0] : n;
}
var Pt = Mt;
function Ft(e) {
	let t = it(e.routes, e), n = e.parseQuery || mt, r = e.stringifyQuery || ht, a = e.history, o = St(), s = St(), c = St(), l = d(xe), u = xe;
	m && e.scrollBehavior && "scrollRestoration" in history && (history.scrollRestoration = "manual");
	let h = v.bind(null, (e) => "" + e), g = v.bind(null, ue), x = v.bind(null, A);
	function ee(e, n) {
		let r, i;
		return He(e) ? (r = t.getRecordMatcher(e), i = n) : i = e, t.addRoute(i, r);
	}
	function te(e) {
		let n = t.getRecordMatcher(e);
		n && t.removeRoute(n);
	}
	function S() {
		return t.getRoutes().map((e) => e.record);
	}
	function ne(e) {
		return !!t.getRecordMatcher(e);
	}
	function C(e, i) {
		if (i = _({}, i || l.value), typeof e == "string") {
			let r = pe(n, e, i.path), o = t.resolve({ path: r.path }, i), s = a.createHref(r.fullPath);
			return _(r, o, {
				params: x(o.params),
				hash: A(r.hash),
				redirectedFrom: void 0,
				href: s
			});
		}
		let o;
		if (e.path != null) o = _({}, e, { path: pe(n, e.path, i.path).path });
		else {
			let t = _({}, e.params);
			for (let e in t) t[e] ?? delete t[e];
			o = _({}, e, { params: g(t) }), i.params = g(i.params);
		}
		let s = t.resolve(o, i), c = e.hash || "";
		s.params = h(x(s.params));
		let u = me(r, _({}, e, {
			hash: se(c),
			path: s.path
		})), d = a.createHref(u);
		return _({
			fullPath: u,
			hash: c,
			query: r === ht ? gt(e.query) : e.query || {}
		}, s, {
			redirectedFrom: void 0,
			href: d
		});
	}
	function w(e) {
		return typeof e == "string" ? pe(n, e, l.value.path) : _({}, e);
	}
	function re(e, t) {
		if (u !== e) return Ge(8, {
			from: t,
			to: e
		});
	}
	function ie(e) {
		return D(e);
	}
	function T(e) {
		return ie(_(w(e), { replace: !0 }));
	}
	function E(e) {
		let t = e.matched[e.matched.length - 1];
		if (t && t.redirect) {
			let { redirect: n } = t, r = typeof n == "function" ? n(e) : n;
			return typeof r == "string" && (r = r.includes("?") || r.includes("#") ? r = w(r) : { path: r }, r.params = {}), _({
				query: e.query,
				hash: e.hash,
				params: r.path == null ? e.params : {}
			}, r);
		}
	}
	function D(e, t) {
		let n = u = C(e), i = l.value, a = e.state, o = e.force, s = e.replace === !0, c = E(n);
		if (c) return D(_(w(c), {
			state: typeof c == "object" ? _({}, a, c.state) : a,
			force: o,
			replace: s
		}), t || n);
		let d = n;
		d.redirectedFrom = t;
		let f;
		return !o && ge(r, i, n) && (f = Ge(16, {
			to: d,
			from: i
		}), be(i, i, !0, !1)), (f ? Promise.resolve(f) : oe(d, i)).catch((e) => Ke(e) ? Ke(e, 2) ? e : ye(e) : _e(e, d, i)).then((e) => {
			if (e) {
				if (Ke(e, 2)) return D(_({ replace: s }, w(e.to), {
					state: typeof e.to == "object" ? _({}, a, e.to.state) : a,
					force: o
				}), t || d);
			} else e = k(d, i, !0, s, a);
			return ce(d, i, e), e;
		});
	}
	function ae(e, t) {
		let n = re(e, t);
		return n ? Promise.reject(n) : Promise.resolve();
	}
	function O(e) {
		let t = Te.values().next().value;
		return t && typeof t.runWithContext == "function" ? t.runWithContext(e) : e();
	}
	function oe(e, t) {
		let n, [r, i, a] = It(e, t);
		n = wt(r.reverse(), "beforeRouteLeave", e, t);
		for (let i of r) i.leaveGuards.forEach((r) => {
			n.push(Ct(r, e, t));
		});
		let c = ae.bind(null, e, t);
		return n.push(c), De(n).then(() => {
			n = [];
			for (let r of o.list()) n.push(Ct(r, e, t));
			return n.push(c), De(n);
		}).then(() => {
			n = wt(i, "beforeRouteUpdate", e, t);
			for (let r of i) r.updateGuards.forEach((r) => {
				n.push(Ct(r, e, t));
			});
			return n.push(c), De(n);
		}).then(() => {
			n = [];
			for (let r of a) if (r.beforeEnter) if (b(r.beforeEnter)) for (let i of r.beforeEnter) n.push(Ct(i, e, t));
			else n.push(Ct(r.beforeEnter, e, t));
			return n.push(c), De(n);
		}).then(() => (e.matched.forEach((e) => e.enterCallbacks = {}), n = wt(a, "beforeRouteEnter", e, t, O), n.push(c), De(n))).then(() => {
			n = [];
			for (let r of s.list()) n.push(Ct(r, e, t));
			return n.push(c), De(n);
		}).catch((e) => Ke(e, 8) ? e : Promise.reject(e));
	}
	function ce(e, t, n) {
		c.list().forEach((r) => O(() => r(e, t, n)));
	}
	function k(e, t, n, r, i) {
		let o = re(e, t);
		if (o) return o;
		let s = t === xe, c = m ? history.state : {};
		n && (r || s ? a.replace(e.fullPath, _({ scroll: s && c && c.scroll }, i)) : a.push(e.fullPath, i)), l.value = e, be(e, t, n, s), ye();
	}
	let le;
	function de() {
		le ||= a.listen((e, t, n) => {
			if (!Ee.listening) return;
			let r = C(e), i = E(r);
			if (i) {
				D(_(i, {
					replace: !0,
					force: !0
				}), r).catch(y);
				return;
			}
			u = r;
			let o = l.value;
			m && Me(Ae(o.fullPath, n.delta), Oe()), oe(r, o).catch((e) => Ke(e, 12) ? e : Ke(e, 2) ? (D(_(w(e.to), { force: !0 }), r).then((e) => {
				Ke(e, 20) && !n.delta && n.type === Se.pop && a.go(-1, !1);
			}).catch(y), Promise.reject()) : (n.delta && a.go(-n.delta, !1), _e(e, r, o))).then((e) => {
				e ||= k(r, o, !1), e && (n.delta && !Ke(e, 8) ? a.go(-n.delta, !1) : n.type === Se.pop && Ke(e, 20) && a.go(-1, !1)), ce(r, o, e);
			}).catch(y);
		});
	}
	let fe = St(), he = St(), j;
	function _e(e, t, n) {
		ye(e);
		let r = he.list();
		return r.length ? r.forEach((r) => r(e, t, n)) : console.error(e), Promise.reject(e);
	}
	function ve() {
		return j && l.value !== xe ? Promise.resolve() : new Promise((e, t) => {
			fe.add([e, t]);
		});
	}
	function ye(e) {
		return j || (j = !e, de(), fe.list().forEach(([t, n]) => e ? n(e) : t()), fe.reset()), e;
	}
	function be(t, n, r, i) {
		let { scrollBehavior: a } = e;
		if (!m || !a) return Promise.resolve();
		let o = !r && Ne(Ae(t.fullPath, 0)) || (i || !r) && history.state && history.state.scroll || null;
		return p().then(() => a(t, n, o)).then((e) => e && ke(e)).catch((e) => _e(e, t, n));
	}
	let Ce = (e) => a.go(e), we, Te = /* @__PURE__ */ new Set(), Ee = {
		currentRoute: l,
		listening: !0,
		addRoute: ee,
		removeRoute: te,
		clearRoutes: t.clearRoutes,
		hasRoute: ne,
		getRoutes: S,
		resolve: C,
		options: e,
		push: ie,
		replace: T,
		go: Ce,
		back: () => Ce(-1),
		forward: () => Ce(1),
		beforeEach: o.add,
		beforeResolve: s.add,
		afterEach: c.add,
		onError: he.add,
		isReady: ve,
		install(e) {
			let t = this;
			e.component("RouterLink", Dt), e.component("RouterView", Pt), e.config.globalProperties.$router = t, Object.defineProperty(e.config.globalProperties, "$route", {
				enumerable: !0,
				get: () => i(l)
			}), m && !we && l.value === xe && (we = !0, ie(a.location).catch((e) => {}));
			let n = {};
			for (let e in xe) Object.defineProperty(n, e, {
				get: () => l.value[e],
				enumerable: !0
			});
			e.provide(yt, t), e.provide(bt, f(n)), e.provide(xt, l);
			let r = e.unmount;
			Te.add(e), e.unmount = function() {
				Te.delete(e), Te.size < 1 && (u = xe, le && le(), le = null, l.value = xe, we = !1, j = !1), r();
			};
		}
	};
	function De(e) {
		return e.reduce((e, t) => e.then(() => O(t)), Promise.resolve());
	}
	return Ee;
}
function It(e, t) {
	let n = [], r = [], i = [], a = Math.max(t.matched.length, e.matched.length);
	for (let o = 0; o < a; o++) {
		let a = t.matched[o];
		a && (e.matched.find((e) => j(e, a)) ? r.push(a) : n.push(a));
		let s = e.matched[o];
		s && (t.matched.find((e) => j(e, s)) || i.push(s));
	}
	return [
		n,
		r,
		i
	];
}
//#endregion
//#region node_modules/axios/lib/helpers/bind.js
function Lt(e, t) {
	return function() {
		return e.apply(t, arguments);
	};
}
//#endregion
//#region node_modules/axios/lib/utils.js
var { toString: Rt } = Object.prototype, { getPrototypeOf: zt } = Object, { iterator: Bt, toStringTag: Vt } = Symbol, Ht = (({ hasOwnProperty: e }) => (t, n) => e.call(t, n))(Object.prototype), Ut = (e, t) => {
	let n = e, r = [];
	for (; n != null && n !== Object.prototype;) {
		if (r.indexOf(n) !== -1) return !1;
		if (r.push(n), Ht(n, t)) return !0;
		n = zt(n);
	}
	return !1;
}, Wt = (e, t) => e != null && Ut(e, t) ? e[t] : void 0, Gt = ((e) => (t) => {
	let n = Rt.call(t);
	return e[n] || (e[n] = n.slice(8, -1).toLowerCase());
})(Object.create(null)), M = (e) => (e = e.toLowerCase(), (t) => Gt(t) === e), Kt = (e) => (t) => typeof t === e, { isArray: qt } = Array, Jt = Kt("undefined");
function Yt(e) {
	return e !== null && !Jt(e) && e.constructor !== null && !Jt(e.constructor) && N(e.constructor.isBuffer) && e.constructor.isBuffer(e);
}
var Xt = M("ArrayBuffer");
function Zt(e) {
	let t;
	return t = typeof ArrayBuffer < "u" && ArrayBuffer.isView ? ArrayBuffer.isView(e) : e && e.buffer && Xt(e.buffer), t;
}
var Qt = Kt("string"), N = Kt("function"), $t = Kt("number"), en = (e) => typeof e == "object" && !!e, tn = (e) => e === !0 || e === !1, nn = (e) => {
	if (!en(e)) return !1;
	let t = zt(e);
	return (t === null || t === Object.prototype || zt(t) === null) && !Ut(e, Vt) && !Ut(e, Bt);
}, rn = (e) => {
	if (!en(e) || Yt(e)) return !1;
	try {
		return Object.keys(e).length === 0 && Object.getPrototypeOf(e) === Object.prototype;
	} catch {
		return !1;
	}
}, an = M("Date"), on = M("File"), sn = (e) => !!(e && e.uri !== void 0), cn = (e) => e && e.getParts !== void 0, ln = M("Blob"), un = M("FileList"), dn = (e) => en(e) && N(e.pipe);
function fn() {
	return typeof globalThis < "u" ? globalThis : typeof self < "u" ? self : typeof window < "u" ? window : typeof global < "u" ? global : {};
}
var pn = fn(), mn = pn.FormData === void 0 ? void 0 : pn.FormData, hn = (e) => {
	if (!e) return !1;
	if (mn && e instanceof mn) return !0;
	let t = zt(e);
	if (!t || t === Object.prototype || !N(e.append)) return !1;
	let n = Gt(e);
	return n === "formdata" || n === "object" && N(e.toString) && e.toString() === "[object FormData]";
}, gn = M("URLSearchParams"), [_n, vn, yn, bn] = [
	"ReadableStream",
	"Request",
	"Response",
	"Headers"
].map(M), xn = (e) => e.trim ? e.trim() : e.replace(/^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g, "");
function Sn(e, t, { allOwnKeys: n = !1 } = {}) {
	if (e == null) return;
	let r, i;
	if (typeof e != "object" && (e = [e]), qt(e)) for (r = 0, i = e.length; r < i; r++) t.call(null, e[r], r, e);
	else {
		if (Yt(e)) return;
		let i = n ? Object.getOwnPropertyNames(e) : Object.keys(e), a = i.length, o;
		for (r = 0; r < a; r++) o = i[r], t.call(null, e[o], o, e);
	}
}
function Cn(e, t) {
	if (Yt(e)) return null;
	t = t.toLowerCase();
	let n = Object.keys(e), r = n.length, i;
	for (; r-- > 0;) if (i = n[r], t === i.toLowerCase()) return i;
	return null;
}
var wn = typeof globalThis < "u" ? globalThis : typeof self < "u" ? self : typeof window < "u" ? window : global, Tn = (e) => !Jt(e) && e !== wn;
function En(...e) {
	let { caseless: t, skipUndefined: n } = Tn(this) && this || {}, r = {}, i = (e, i) => {
		if (i === "__proto__" || i === "constructor" || i === "prototype") return;
		let a = t && typeof i == "string" && Cn(r, i) || i, o = Ht(r, a) ? r[a] : void 0;
		nn(o) && nn(e) ? r[a] = En(o, e) : nn(e) ? r[a] = En({}, e) : qt(e) ? r[a] = e.slice() : (!n || !Jt(e)) && (r[a] = e);
	};
	for (let t = 0, n = e.length; t < n; t++) {
		let n = e[t];
		if (!n || Yt(n) || (Sn(n, i), typeof n != "object" || qt(n))) continue;
		let r = Object.getOwnPropertySymbols(n);
		for (let e = 0; e < r.length; e++) {
			let t = r[e];
			Rn.call(n, t) && i(n[t], t);
		}
	}
	return r;
}
var Dn = (e, t, n, { allOwnKeys: r } = {}) => (Sn(t, (t, r) => {
	n && N(t) ? Object.defineProperty(e, r, {
		__proto__: null,
		value: Lt(t, n),
		writable: !0,
		enumerable: !0,
		configurable: !0
	}) : Object.defineProperty(e, r, {
		__proto__: null,
		value: t,
		writable: !0,
		enumerable: !0,
		configurable: !0
	});
}, { allOwnKeys: r }), e), On = (e) => (e.charCodeAt(0) === 65279 && (e = e.slice(1)), e), kn = (e, t, n, r) => {
	e.prototype = Object.create(t.prototype, r), Object.defineProperty(e.prototype, "constructor", {
		__proto__: null,
		value: e,
		writable: !0,
		enumerable: !1,
		configurable: !0
	}), Object.defineProperty(e, "super", {
		__proto__: null,
		value: t.prototype
	}), n && Object.assign(e.prototype, n);
}, An = (e, t, n, r) => {
	let i, a, o, s = {};
	if (t ||= {}, e == null) return t;
	do {
		for (i = Object.getOwnPropertyNames(e), a = i.length; a-- > 0;) o = i[a], (!r || r(o, e, t)) && !s[o] && (t[o] = e[o], s[o] = !0);
		e = n !== !1 && zt(e);
	} while (e && (!n || n(e, t)) && e !== Object.prototype);
	return t;
}, jn = (e, t, n) => {
	e = String(e), (n === void 0 || n > e.length) && (n = e.length), n -= t.length;
	let r = e.indexOf(t, n);
	return r !== -1 && r === n;
}, Mn = (e) => {
	if (!e) return null;
	if (qt(e)) return e;
	let t = e.length;
	if (!$t(t)) return null;
	let n = Array(t);
	for (; t-- > 0;) n[t] = e[t];
	return n;
}, Nn = ((e) => (t) => e && t instanceof e)(typeof Uint8Array < "u" && zt(Uint8Array)), Pn = (e, t) => {
	let n = (e && e[Bt]).call(e), r;
	for (; (r = n.next()) && !r.done;) {
		let n = r.value;
		t.call(e, n[0], n[1]);
	}
}, Fn = (e, t) => {
	let n, r = [];
	for (; (n = e.exec(t)) !== null;) r.push(n);
	return r;
}, In = M("HTMLFormElement"), Ln = (e) => e.toLowerCase().replace(/[-_\s]([a-z\d])(\w*)/g, function(e, t, n) {
	return t.toUpperCase() + n;
}), { propertyIsEnumerable: Rn } = Object.prototype, zn = M("RegExp"), Bn = (e, t) => {
	let n = Object.getOwnPropertyDescriptors(e), r = {};
	Sn(n, (n, i) => {
		let a;
		(a = t(n, i, e)) !== !1 && (r[i] = a || n);
	}), Object.defineProperties(e, r);
}, Vn = (e) => {
	Bn(e, (t, n) => {
		if (N(e) && [
			"arguments",
			"caller",
			"callee"
		].includes(n)) return !1;
		let r = e[n];
		if (N(r)) {
			if (t.enumerable = !1, "writable" in t) {
				t.writable = !1;
				return;
			}
			t.set ||= () => {
				throw Error("Can not rewrite read-only method '" + n + "'");
			};
		}
	});
}, Hn = (e, t) => {
	let n = {}, r = (e) => {
		e.forEach((e) => {
			n[e] = !0;
		});
	};
	return qt(e) ? r(e) : r(String(e).split(t)), n;
}, Un = () => {}, Wn = (e, t) => e != null && Number.isFinite(e = +e) ? e : t;
function Gn(e) {
	return !!(e && N(e.append) && e[Vt] === "FormData" && e[Bt]);
}
var Kn = (e) => {
	let t = /* @__PURE__ */ new WeakSet(), n = (e) => {
		if (en(e)) {
			if (t.has(e)) return;
			if (Yt(e)) return e;
			if (!("toJSON" in e)) {
				t.add(e);
				let r = qt(e) ? [] : {};
				return Sn(e, (e, t) => {
					let i = n(e);
					!Jt(i) && (r[t] = i);
				}), t.delete(e), r;
			}
		}
		return e;
	};
	return n(e);
}, qn = M("AsyncFunction"), Jn = (e) => e && (en(e) || N(e)) && N(e.then) && N(e.catch), Yn = ((e, t) => e ? setImmediate : t ? ((e, t) => (wn.addEventListener("message", ({ source: n, data: r }) => {
	n === wn && r === e && t.length && t.shift()();
}, !1), (n) => {
	t.push(n), wn.postMessage(e, "*");
}))(`axios@${Math.random()}`, []) : (e) => setTimeout(e))(typeof setImmediate == "function", N(wn.postMessage)), Xn = typeof queueMicrotask < "u" ? queueMicrotask.bind(wn) : typeof process < "u" && process.nextTick || Yn, Zn = (e) => e != null && N(e[Bt]), P = {
	isArray: qt,
	isArrayBuffer: Xt,
	isBuffer: Yt,
	isFormData: hn,
	isArrayBufferView: Zt,
	isString: Qt,
	isNumber: $t,
	isBoolean: tn,
	isObject: en,
	isPlainObject: nn,
	isEmptyObject: rn,
	isReadableStream: _n,
	isRequest: vn,
	isResponse: yn,
	isHeaders: bn,
	isUndefined: Jt,
	isDate: an,
	isFile: on,
	isReactNativeBlob: sn,
	isReactNative: cn,
	isBlob: ln,
	isRegExp: zn,
	isFunction: N,
	isStream: dn,
	isURLSearchParams: gn,
	isTypedArray: Nn,
	isFileList: un,
	forEach: Sn,
	merge: En,
	extend: Dn,
	trim: xn,
	stripBOM: On,
	inherits: kn,
	toFlatObject: An,
	kindOf: Gt,
	kindOfTest: M,
	endsWith: jn,
	toArray: Mn,
	forEachEntry: Pn,
	matchAll: Fn,
	isHTMLForm: In,
	hasOwnProperty: Ht,
	hasOwnProp: Ht,
	hasOwnInPrototypeChain: Ut,
	getSafeProp: Wt,
	reduceDescriptors: Bn,
	freezeMethods: Vn,
	toObjectSet: Hn,
	toCamelCase: Ln,
	noop: Un,
	toFiniteNumber: Wn,
	findKey: Cn,
	global: wn,
	isContextDefined: Tn,
	isSpecCompliantForm: Gn,
	toJSONObject: Kn,
	isAsyncFn: qn,
	isThenable: Jn,
	setImmediate: Yn,
	asap: Xn,
	isIterable: Zn,
	isSafeIterable: (e) => e != null && Ut(e, Bt) && Zn(e)
}, Qn = P.toObjectSet([
	"age",
	"authorization",
	"content-length",
	"content-type",
	"etag",
	"expires",
	"from",
	"host",
	"if-modified-since",
	"if-unmodified-since",
	"last-modified",
	"location",
	"max-forwards",
	"proxy-authorization",
	"referer",
	"retry-after",
	"user-agent"
]), $n = (e) => {
	let t = {}, n, r, i;
	return e && e.split("\n").forEach(function(e) {
		i = e.indexOf(":"), n = e.substring(0, i).trim().toLowerCase(), r = e.substring(i + 1).trim(), !(!n || t[n] && Qn[n]) && (n === "set-cookie" ? t[n] ? t[n].push(r) : t[n] = [r] : t[n] = t[n] ? t[n] + ", " + r : r);
	}), t;
};
//#endregion
//#region node_modules/axios/lib/helpers/sanitizeHeaderValue.js
function er(e) {
	let t = 0, n = e.length;
	for (; t < n;) {
		let n = e.charCodeAt(t);
		if (n !== 9 && n !== 32) break;
		t += 1;
	}
	for (; n > t;) {
		let t = e.charCodeAt(n - 1);
		if (t !== 9 && t !== 32) break;
		--n;
	}
	return t === 0 && n === e.length ? e : e.slice(t, n);
}
var tr = /* @__PURE__ */ RegExp("[\\u0000-\\u0008\\u000a-\\u001f\\u007f]+", "g"), nr = /* @__PURE__ */ RegExp("[^\\u0009\\u0020-\\u007e\\u0080-\\u00ff]+", "g");
function rr(e, t) {
	return P.isArray(e) ? e.map((e) => rr(e, t)) : er(String(e).replace(t, ""));
}
var ir = (e) => rr(e, tr), ar = (e) => rr(e, nr);
function or(e) {
	let t = Object.create(null);
	return P.forEach(e.toJSON(), (e, n) => {
		t[n] = ar(e);
	}), t;
}
//#endregion
//#region node_modules/axios/lib/core/AxiosHeaders.js
var sr = Symbol("internals");
function cr(e) {
	return e && String(e).trim().toLowerCase();
}
function lr(e) {
	return e === !1 || e == null ? e : P.isArray(e) ? e.map(lr) : ir(String(e));
}
function ur(e) {
	let t = Object.create(null), n = /([^\s,;=]+)\s*(?:=\s*([^,;]+))?/g, r;
	for (; r = n.exec(e);) t[r[1]] = r[2];
	return t;
}
var dr = (e) => /^[-_a-zA-Z0-9^`|~,!#$%&'*+.]+$/.test(e.trim());
function fr(e, t, n, r, i) {
	if (P.isFunction(r)) return r.call(this, t, n);
	if (i && (t = n), P.isString(t)) {
		if (P.isString(r)) return t.indexOf(r) !== -1;
		if (P.isRegExp(r)) return r.test(t);
	}
}
function pr(e) {
	return e.trim().toLowerCase().replace(/([a-z\d])(\w*)/g, (e, t, n) => t.toUpperCase() + n);
}
function mr(e, t) {
	let n = P.toCamelCase(" " + t);
	[
		"get",
		"set",
		"has"
	].forEach((r) => {
		Object.defineProperty(e, r + n, {
			__proto__: null,
			value: function(e, n, i) {
				return this[r].call(this, t, e, n, i);
			},
			configurable: !0
		});
	});
}
var F = class {
	constructor(e) {
		e && this.set(e);
	}
	set(e, t, n) {
		let r = this;
		function i(e, t, n) {
			let i = cr(t);
			if (!i) return;
			let a = P.findKey(r, i);
			(!a || r[a] === void 0 || n === !0 || n === void 0 && r[a] !== !1) && (r[a || t] = lr(e));
		}
		let a = (e, t) => P.forEach(e, (e, n) => i(e, n, t));
		if (P.isPlainObject(e) || e instanceof this.constructor) a(e, t);
		else if (P.isString(e) && (e = e.trim()) && !dr(e)) a($n(e), t);
		else if (P.isObject(e) && P.isSafeIterable(e)) {
			let n = Object.create(null), r, i;
			for (let t of e) {
				if (!P.isArray(t)) throw TypeError("Object iterator must return a key-value pair");
				i = t[0], P.hasOwnProp(n, i) ? (r = n[i], n[i] = P.isArray(r) ? [...r, t[1]] : [r, t[1]]) : n[i] = t[1];
			}
			a(n, t);
		} else e != null && i(t, e, n);
		return this;
	}
	get(e, t) {
		if (e = cr(e), e) {
			let n = P.findKey(this, e);
			if (n) {
				let e = this[n];
				if (!t) return e;
				if (t === !0) return ur(e);
				if (P.isFunction(t)) return t.call(this, e, n);
				if (P.isRegExp(t)) return t.exec(e);
				throw TypeError("parser must be boolean|regexp|function");
			}
		}
	}
	has(e, t) {
		if (e = cr(e), e) {
			let n = P.findKey(this, e);
			return !!(n && this[n] !== void 0 && (!t || fr(this, this[n], n, t)));
		}
		return !1;
	}
	delete(e, t) {
		let n = this, r = !1;
		function i(e) {
			if (e = cr(e), e) {
				let i = P.findKey(n, e);
				i && (!t || fr(n, n[i], i, t)) && (delete n[i], r = !0);
			}
		}
		return P.isArray(e) ? e.forEach(i) : i(e), r;
	}
	clear(e) {
		let t = Object.keys(this), n = t.length, r = !1;
		for (; n--;) {
			let i = t[n];
			(!e || fr(this, this[i], i, e, !0)) && (delete this[i], r = !0);
		}
		return r;
	}
	normalize(e) {
		let t = this, n = {};
		return P.forEach(this, (r, i) => {
			let a = P.findKey(n, i);
			if (a) {
				t[a] = lr(r), delete t[i];
				return;
			}
			let o = e ? pr(i) : String(i).trim();
			o !== i && delete t[i], t[o] = lr(r), n[o] = !0;
		}), this;
	}
	concat(...e) {
		return this.constructor.concat(this, ...e);
	}
	toJSON(e) {
		let t = Object.create(null);
		return P.forEach(this, (n, r) => {
			n != null && n !== !1 && (t[r] = e && P.isArray(n) ? n.join(", ") : n);
		}), t;
	}
	[Symbol.iterator]() {
		return Object.entries(this.toJSON())[Symbol.iterator]();
	}
	toString() {
		return Object.entries(this.toJSON()).map(([e, t]) => e + ": " + t).join("\n");
	}
	getSetCookie() {
		return this.get("set-cookie") || [];
	}
	get [Symbol.toStringTag]() {
		return "AxiosHeaders";
	}
	static from(e) {
		return e instanceof this ? e : new this(e);
	}
	static concat(e, ...t) {
		let n = new this(e);
		return t.forEach((e) => n.set(e)), n;
	}
	static accessor(e) {
		let t = (this[sr] = this[sr] = { accessors: {} }).accessors, n = this.prototype;
		function r(e) {
			let r = cr(e);
			t[r] || (mr(n, e), t[r] = !0);
		}
		return P.isArray(e) ? e.forEach(r) : r(e), this;
	}
};
F.accessor([
	"Content-Type",
	"Content-Length",
	"Accept",
	"Accept-Encoding",
	"User-Agent",
	"Authorization"
]), P.reduceDescriptors(F.prototype, ({ value: e }, t) => {
	let n = t[0].toUpperCase() + t.slice(1);
	return {
		get: () => e,
		set(e) {
			this[n] = e;
		}
	};
}), P.freezeMethods(F);
//#endregion
//#region node_modules/axios/lib/core/AxiosError.js
var hr = "[REDACTED ****]";
function gr(e) {
	if (P.hasOwnProp(e, "toJSON")) return !0;
	let t = Object.getPrototypeOf(e);
	for (; t && t !== Object.prototype;) {
		if (P.hasOwnProp(t, "toJSON")) return !0;
		t = Object.getPrototypeOf(t);
	}
	return !1;
}
function _r(e, t) {
	let n = new Set(t.map((e) => String(e).toLowerCase())), r = [], i = (e) => {
		if (typeof e != "object" || !e || P.isBuffer(e)) return e;
		if (r.indexOf(e) !== -1) return;
		e instanceof F && (e = e.toJSON()), r.push(e);
		let t;
		if (P.isArray(e)) t = [], e.forEach((e, n) => {
			let r = i(e);
			P.isUndefined(r) || (t[n] = r);
		});
		else {
			if (!P.isPlainObject(e) && gr(e)) return r.pop(), e;
			t = Object.create(null);
			for (let [r, a] of Object.entries(e)) {
				let e = n.has(r.toLowerCase()) ? hr : i(a);
				P.isUndefined(e) || (t[r] = e);
			}
		}
		return r.pop(), t;
	};
	return i(e);
}
var I = class e extends Error {
	static from(t, n, r, i, a, o) {
		let s = new e(t.message, n || t.code, r, i, a);
		return Object.defineProperty(s, "cause", {
			__proto__: null,
			value: t,
			writable: !0,
			enumerable: !1,
			configurable: !0
		}), s.name = t.name, t.status != null && s.status == null && (s.status = t.status), o && Object.assign(s, o), s;
	}
	constructor(e, t, n, r, i) {
		super(e), Object.defineProperty(this, "message", {
			__proto__: null,
			value: e,
			enumerable: !0,
			writable: !0,
			configurable: !0
		}), this.name = "AxiosError", this.isAxiosError = !0, t && (this.code = t), n && (this.config = n), r && (this.request = r), i && (this.response = i, this.status = i.status);
	}
	toJSON() {
		let e = this.config, t = e && P.hasOwnProp(e, "redact") ? e.redact : void 0, n = P.isArray(t) && t.length > 0 ? _r(e, t) : P.toJSONObject(e);
		return {
			message: this.message,
			name: this.name,
			description: this.description,
			number: this.number,
			fileName: this.fileName,
			lineNumber: this.lineNumber,
			columnNumber: this.columnNumber,
			stack: this.stack,
			config: n,
			code: this.code,
			status: this.status
		};
	}
};
I.ERR_BAD_OPTION_VALUE = "ERR_BAD_OPTION_VALUE", I.ERR_BAD_OPTION = "ERR_BAD_OPTION", I.ECONNABORTED = "ECONNABORTED", I.ETIMEDOUT = "ETIMEDOUT", I.ECONNREFUSED = "ECONNREFUSED", I.ERR_NETWORK = "ERR_NETWORK", I.ERR_FR_TOO_MANY_REDIRECTS = "ERR_FR_TOO_MANY_REDIRECTS", I.ERR_DEPRECATED = "ERR_DEPRECATED", I.ERR_BAD_RESPONSE = "ERR_BAD_RESPONSE", I.ERR_BAD_REQUEST = "ERR_BAD_REQUEST", I.ERR_CANCELED = "ERR_CANCELED", I.ERR_NOT_SUPPORT = "ERR_NOT_SUPPORT", I.ERR_INVALID_URL = "ERR_INVALID_URL", I.ERR_FORM_DATA_DEPTH_EXCEEDED = "ERR_FORM_DATA_DEPTH_EXCEEDED";
function vr(e) {
	return P.isPlainObject(e) || P.isArray(e);
}
function yr(e) {
	return P.endsWith(e, "[]") ? e.slice(0, -2) : e;
}
function br(e, t, n) {
	return e ? e.concat(t).map(function(e, t) {
		return e = yr(e), !n && t ? "[" + e + "]" : e;
	}).join(n ? "." : "") : t;
}
function xr(e) {
	return P.isArray(e) && !e.some(vr);
}
var Sr = P.toFlatObject(P, {}, null, function(e) {
	return /^is[A-Z]/.test(e);
});
function Cr(e, t, n) {
	if (!P.isObject(e)) throw TypeError("target must be an object");
	t ||= new FormData(), n = P.toFlatObject(n, {
		metaTokens: !0,
		dots: !1,
		indexes: !1
	}, !1, function(e, t) {
		return !P.isUndefined(t[e]);
	});
	let r = n.metaTokens, i = n.visitor || m, a = n.dots, o = n.indexes, s = n.Blob || typeof Blob < "u" && Blob, c = n.maxDepth === void 0 ? 100 : n.maxDepth, l = s && P.isSpecCompliantForm(t), u = [];
	if (!P.isFunction(i)) throw TypeError("visitor must be a function");
	function d(e) {
		if (e === null) return "";
		if (P.isDate(e)) return e.toISOString();
		if (P.isBoolean(e)) return e.toString();
		if (!l && P.isBlob(e)) throw new I("Blob is not supported. Use a Buffer instead.");
		if (P.isArrayBuffer(e) || P.isTypedArray(e)) {
			if (l && typeof s == "function") return new s([e]);
			if (typeof Buffer < "u") return Buffer.from(e);
			throw new I("Blob is not supported. Use a Buffer instead.", I.ERR_NOT_SUPPORT);
		}
		return e;
	}
	function f(e) {
		if (e > c) throw new I("Object is too deeply nested (" + e + " levels). Max depth: " + c, I.ERR_FORM_DATA_DEPTH_EXCEEDED);
	}
	function p(e, t) {
		if (c === Infinity) return JSON.stringify(e);
		let n = [];
		return JSON.stringify(e, function(e, r) {
			if (!P.isObject(r)) return r;
			for (; n.length && n[n.length - 1] !== this;) n.pop();
			return n.push(r), f(t + n.length - 1), r;
		});
	}
	function m(e, n, i) {
		let s = e;
		if (P.isReactNative(t) && P.isReactNativeBlob(e)) return t.append(br(i, n, a), d(e)), !1;
		if (e && !i && typeof e == "object") {
			if (P.endsWith(n, "{}")) n = r ? n : n.slice(0, -2), e = p(e, 1);
			else if (P.isArray(e) && xr(e) || (P.isFileList(e) || P.endsWith(n, "[]")) && (s = P.toArray(e))) return n = yr(n), s.forEach(function(e, r) {
				!(P.isUndefined(e) || e === null) && t.append(o === !0 ? br([n], r, a) : o === null ? n : n + "[]", d(e));
			}), !1;
		}
		return vr(e) ? !0 : (t.append(br(i, n, a), d(e)), !1);
	}
	let h = Object.assign(Sr, {
		defaultVisitor: m,
		convertValue: d,
		isVisitable: vr
	});
	function g(e, n, r = 0) {
		if (!P.isUndefined(e)) {
			if (f(r), u.indexOf(e) !== -1) throw Error("Circular reference detected in " + n.join("."));
			u.push(e), P.forEach(e, function(e, a) {
				(!(P.isUndefined(e) || e === null) && i.call(t, e, P.isString(a) ? a.trim() : a, n, h)) === !0 && g(e, n ? n.concat(a) : [a], r + 1);
			}), u.pop();
		}
	}
	if (!P.isObject(e)) throw TypeError("data must be an object");
	return g(e), t;
}
//#endregion
//#region node_modules/axios/lib/helpers/AxiosURLSearchParams.js
function wr(e) {
	let t = {
		"!": "%21",
		"'": "%27",
		"(": "%28",
		")": "%29",
		"~": "%7E",
		"%20": "+"
	};
	return encodeURIComponent(e).replace(/[!'()~]|%20/g, function(e) {
		return t[e];
	});
}
function Tr(e, t) {
	this._pairs = [], e && Cr(e, this, t);
}
var Er = Tr.prototype;
Er.append = function(e, t) {
	this._pairs.push([e, t]);
}, Er.toString = function(e) {
	let t = e ? (t) => e.call(this, t, wr) : wr;
	return this._pairs.map(function(e) {
		return t(e[0]) + "=" + t(e[1]);
	}, "").join("&");
};
//#endregion
//#region node_modules/axios/lib/helpers/buildURL.js
function Dr(e) {
	return encodeURIComponent(e).replace(/%3A/gi, ":").replace(/%24/g, "$").replace(/%2C/gi, ",").replace(/%20/g, "+");
}
function Or(e, t, n) {
	if (!t) return e;
	e ||= "";
	let r = P.isFunction(n) ? { serialize: n } : n, i = P.getSafeProp(r, "encode") || Dr, a = P.getSafeProp(r, "serialize"), o;
	if (o = a ? a(t, r) : P.isURLSearchParams(t) ? t.toString() : new Tr(t, r).toString(i), o) {
		let t = e.indexOf("#");
		t !== -1 && (e = e.slice(0, t)), e += (e.indexOf("?") === -1 ? "?" : "&") + o;
	}
	return e;
}
//#endregion
//#region node_modules/axios/lib/core/InterceptorManager.js
var kr = class {
	constructor() {
		this.handlers = [];
	}
	use(e, t, n) {
		return this.handlers.push({
			fulfilled: e,
			rejected: t,
			synchronous: n ? n.synchronous : !1,
			runWhen: n ? n.runWhen : null
		}), this.handlers.length - 1;
	}
	eject(e) {
		this.handlers[e] && (this.handlers[e] = null);
	}
	clear() {
		this.handlers &&= [];
	}
	forEach(e) {
		P.forEach(this.handlers, function(t) {
			t !== null && e(t);
		});
	}
}, Ar = {
	silentJSONParsing: !0,
	forcedJSONParsing: !0,
	clarifyTimeoutError: !1,
	legacyInterceptorReqResOrdering: !0,
	advertiseZstdAcceptEncoding: !1,
	validateStatusUndefinedResolves: !0
}, jr = {
	isBrowser: !0,
	classes: {
		URLSearchParams: typeof URLSearchParams < "u" ? URLSearchParams : Tr,
		FormData: typeof FormData < "u" ? FormData : null,
		Blob: typeof Blob < "u" ? Blob : null
	},
	protocols: [
		"http",
		"https",
		"file",
		"blob",
		"url",
		"data"
	]
}, Mr = /* @__PURE__ */ t({
	hasBrowserEnv: () => Nr,
	hasStandardBrowserEnv: () => Fr,
	hasStandardBrowserWebWorkerEnv: () => Ir,
	navigator: () => Pr,
	origin: () => Lr
}), Nr = typeof window < "u" && typeof document < "u", Pr = typeof navigator == "object" && navigator || void 0, Fr = Nr && (!Pr || [
	"ReactNative",
	"NativeScript",
	"NS"
].indexOf(Pr.product) < 0), Ir = typeof WorkerGlobalScope < "u" && self instanceof WorkerGlobalScope && typeof self.importScripts == "function", Lr = Nr && window.location.href || "http://localhost", L = {
	...Mr,
	...jr
};
//#endregion
//#region node_modules/axios/lib/helpers/toURLEncodedForm.js
function Rr(e, t) {
	return Cr(e, new L.classes.URLSearchParams(), {
		visitor: function(e, t, n, r) {
			return L.isNode && P.isBuffer(e) ? (this.append(t, e.toString("base64")), !1) : r.defaultVisitor.apply(this, arguments);
		},
		...t
	});
}
//#endregion
//#region node_modules/axios/lib/helpers/formDataToJSON.js
var zr = 100;
function Br(e) {
	if (e > zr) throw new I("FormData field is too deeply nested (" + e + " levels). Max depth: " + zr, I.ERR_FORM_DATA_DEPTH_EXCEEDED);
}
function Vr(e) {
	let t = [], n = /\w+|\[(\w*)]/g, r;
	for (; (r = n.exec(e)) !== null;) Br(t.length), t.push(r[0] === "[]" ? "" : r[1] || r[0]);
	return t;
}
function Hr(e) {
	let t = {}, n = Object.keys(e), r, i = n.length, a;
	for (r = 0; r < i; r++) a = n[r], t[a] = e[a];
	return t;
}
function Ur(e) {
	function t(e, n, r, i) {
		Br(i);
		let a = e[i++];
		if (a === "__proto__") return !0;
		let o = Number.isFinite(+a), s = i >= e.length;
		return a = !a && P.isArray(r) ? r.length : a, s ? (P.hasOwnProp(r, a) ? r[a] = P.isArray(r[a]) ? r[a].concat(n) : [r[a], n] : r[a] = n, !o) : ((!P.hasOwnProp(r, a) || !P.isObject(r[a])) && (r[a] = []), t(e, n, r[a], i) && P.isArray(r[a]) && (r[a] = Hr(r[a])), !o);
	}
	if (P.isFormData(e) && P.isFunction(e.entries)) {
		let n = {};
		return P.forEachEntry(e, (e, r) => {
			t(Vr(e), r, n, 0);
		}), n;
	}
	return null;
}
//#endregion
//#region node_modules/axios/lib/defaults/index.js
var Wr = (e, t) => e != null && P.hasOwnProp(e, t) ? e[t] : void 0;
function Gr(e, t, n) {
	if (P.isString(e)) try {
		return (t || JSON.parse)(e), P.trim(e);
	} catch (e) {
		if (e.name !== "SyntaxError") throw e;
	}
	return (n || JSON.stringify)(e);
}
var Kr = {
	transitional: Ar,
	adapter: [
		"xhr",
		"http",
		"fetch"
	],
	transformRequest: [function(e, t) {
		let n = t.getContentType() || "", r = n.indexOf("application/json") > -1, i = P.isObject(e);
		if (i && P.isHTMLForm(e) && (e = new FormData(e)), P.isFormData(e)) return r ? JSON.stringify(Ur(e)) : e;
		if (P.isArrayBuffer(e) || P.isBuffer(e) || P.isStream(e) || P.isFile(e) || P.isBlob(e) || P.isReadableStream(e)) return e;
		if (P.isArrayBufferView(e)) return e.buffer;
		if (P.isURLSearchParams(e)) return t.setContentType("application/x-www-form-urlencoded;charset=utf-8", !1), e.toString();
		let a;
		if (i) {
			let t = Wr(this, "formSerializer");
			if (n.indexOf("application/x-www-form-urlencoded") > -1) return Rr(e, t).toString();
			if ((a = P.isFileList(e)) || n.indexOf("multipart/form-data") > -1) {
				let n = Wr(this, "env"), r = n && n.FormData;
				return Cr(a ? { "files[]": e } : e, r && new r(), t);
			}
		}
		return i || r ? (t.setContentType("application/json", !1), Gr(e)) : e;
	}],
	transformResponse: [function(e) {
		let t = Wr(this, "transitional") || Kr.transitional, n = t && t.forcedJSONParsing, r = Wr(this, "responseType"), i = r === "json";
		if (P.isResponse(e) || P.isReadableStream(e)) return e;
		if (e && P.isString(e) && (n && !r || i)) {
			let n = !(t && t.silentJSONParsing) && i;
			try {
				return JSON.parse(e, Wr(this, "parseReviver"));
			} catch (e) {
				if (n) throw e.name === "SyntaxError" ? I.from(e, I.ERR_BAD_RESPONSE, this, null, Wr(this, "response")) : e;
			}
		}
		return e;
	}],
	timeout: 0,
	xsrfCookieName: "XSRF-TOKEN",
	xsrfHeaderName: "X-XSRF-TOKEN",
	maxContentLength: -1,
	maxBodyLength: -1,
	env: {
		FormData: L.classes.FormData,
		Blob: L.classes.Blob
	},
	validateStatus: function(e) {
		return e >= 200 && e < 300;
	},
	headers: { common: {
		Accept: "application/json, text/plain, */*",
		"Content-Type": void 0
	} }
};
P.forEach([
	"delete",
	"get",
	"head",
	"post",
	"put",
	"patch",
	"query"
], (e) => {
	Kr.headers[e] = {};
});
//#endregion
//#region node_modules/axios/lib/core/transformData.js
function qr(e, t) {
	let n = this || Kr, r = t || n, i = F.from(r.headers), a = r.data;
	return P.forEach(e, function(e) {
		a = e.call(n, a, i.normalize(), t ? t.status : void 0);
	}), i.normalize(), a;
}
//#endregion
//#region node_modules/axios/lib/cancel/isCancel.js
function Jr(e) {
	return !!(e && e.__CANCEL__);
}
//#endregion
//#region node_modules/axios/lib/cancel/CanceledError.js
var Yr = class extends I {
	constructor(e, t, n) {
		super(e ?? "canceled", I.ERR_CANCELED, t, n), this.name = "CanceledError", this.__CANCEL__ = !0;
	}
};
//#endregion
//#region node_modules/axios/lib/core/settle.js
function Xr(e, t, n) {
	let r = n.config.validateStatus;
	!n.status || !r || r(n.status) ? e(n) : t(new I("Request failed with status code " + n.status, n.status >= 400 && n.status < 500 ? I.ERR_BAD_REQUEST : I.ERR_BAD_RESPONSE, n.config, n.request, n));
}
//#endregion
//#region node_modules/axios/lib/helpers/parseProtocol.js
function Zr(e) {
	let t = /^([-+\w]{1,25}):(?:\/\/)?/.exec(e);
	return t && t[1] || "";
}
//#endregion
//#region node_modules/axios/lib/helpers/speedometer.js
function Qr(e, t) {
	e ||= 10;
	let n = Array(e), r = Array(e), i = 0, a = 0, o;
	return t = t === void 0 ? 1e3 : t, function(s) {
		let c = Date.now(), l = r[a];
		o ||= c, n[i] = s, r[i] = c;
		let u = a, d = 0;
		for (; u !== i;) d += n[u++], u %= e;
		if (i = (i + 1) % e, i === a && (a = (a + 1) % e), c - o < t) return;
		let f = l && c - l;
		return f ? Math.round(d * 1e3 / f) : void 0;
	};
}
//#endregion
//#region node_modules/axios/lib/helpers/throttle.js
function $r(e, t) {
	let n = 0, r = 1e3 / t, i, a, o = (t, r = Date.now()) => {
		n = r, i = null, a &&= (clearTimeout(a), null), e(...t);
	};
	return [(...e) => {
		let t = Date.now(), s = t - n;
		s >= r ? o(e, t) : (i = e, a ||= setTimeout(() => {
			a = null, o(i);
		}, r - s));
	}, () => i && o(i)];
}
//#endregion
//#region node_modules/axios/lib/helpers/progressEventReducer.js
var ei = (e, t, n = 3) => {
	let r = 0, i = Qr(50, 250);
	return $r((n) => {
		if (!n || typeof n.loaded != "number") return;
		let a = n.loaded, o = n.lengthComputable ? n.total : void 0, s = o == null ? a : Math.min(a, o), c = Math.max(0, s - r), l = i(c);
		r = Math.max(r, s), e({
			loaded: s,
			total: o,
			progress: o ? s / o : void 0,
			bytes: c,
			rate: l || void 0,
			estimated: l && o ? (o - s) / l : void 0,
			event: n,
			lengthComputable: o != null,
			[t ? "download" : "upload"]: !0
		});
	}, n);
}, ti = (e, t) => {
	let n = e != null;
	return [(r) => t[0]({
		lengthComputable: n,
		total: e,
		loaded: r
	}), t[1]];
}, ni = (e) => (...t) => P.asap(() => e(...t)), ri = L.hasStandardBrowserEnv ? ((e, t) => (n) => (n = new URL(n, L.origin), e.protocol === n.protocol && e.host === n.host && (t || e.port === n.port)))(new URL(L.origin), L.navigator && /(msie|trident)/i.test(L.navigator.userAgent)) : () => !0, ii = L.hasStandardBrowserEnv ? {
	write(e, t, n, r, i, a, o) {
		if (typeof document > "u") return;
		let s = [`${e}=${encodeURIComponent(t)}`];
		P.isNumber(n) && s.push(`expires=${new Date(n).toUTCString()}`), P.isString(r) && s.push(`path=${r}`), P.isString(i) && s.push(`domain=${i}`), a === !0 && s.push("secure"), P.isString(o) && s.push(`SameSite=${o}`), document.cookie = s.join("; ");
	},
	read(e) {
		if (typeof document > "u") return null;
		let t = document.cookie.split(";");
		for (let n = 0; n < t.length; n++) {
			let r = t[n].replace(/^\s+/, ""), i = r.indexOf("=");
			if (i !== -1 && r.slice(0, i) === e) try {
				return decodeURIComponent(r.slice(i + 1));
			} catch {
				return r.slice(i + 1);
			}
		}
		return null;
	},
	remove(e) {
		this.write(e, "", Date.now() - 864e5, "/");
	}
} : {
	write() {},
	read() {
		return null;
	},
	remove() {}
};
//#endregion
//#region node_modules/axios/lib/helpers/isAbsoluteURL.js
function ai(e) {
	return typeof e == "string" ? /^([a-z][a-z\d+\-.]*:)?\/\//i.test(e) : !1;
}
//#endregion
//#region node_modules/axios/lib/helpers/combineURLs.js
function oi(e, t) {
	return t ? e.replace(/\/?\/$/, "") + "/" + t.replace(/^\/+/, "") : e;
}
//#endregion
//#region node_modules/axios/lib/core/buildFullPath.js
var si = /^https?:(?!\/\/)/i, ci = /[\t\n\r]/g;
function li(e) {
	let t = 0;
	for (; t < e.length && e.charCodeAt(t) <= 32;) t++;
	return e.slice(t);
}
function ui(e) {
	return li(e).replace(ci, "");
}
function di(e, t) {
	if (typeof e == "string" && si.test(ui(e))) throw new I("Invalid URL: missing \"//\" after protocol", I.ERR_INVALID_URL, t);
}
function fi(e, t, n, r) {
	di(t, r);
	let i = !ai(t);
	return e && (i || n === !1) ? (di(e, r), oi(e, t)) : t;
}
//#endregion
//#region node_modules/axios/lib/core/mergeConfig.js
var pi = (e) => e instanceof F ? { ...e } : e;
function mi(e, t) {
	e ||= {}, t ||= {};
	let n = Object.create(null);
	Object.defineProperty(n, "hasOwnProperty", {
		__proto__: null,
		value: Object.prototype.hasOwnProperty,
		enumerable: !1,
		writable: !0,
		configurable: !0
	});
	function r(e, t, n, r) {
		return P.isPlainObject(e) && P.isPlainObject(t) ? P.merge.call({ caseless: r }, e, t) : P.isPlainObject(t) ? P.merge({}, t) : P.isArray(t) ? t.slice() : t;
	}
	function i(e, t, n, i) {
		if (!P.isUndefined(t)) return r(e, t, n, i);
		if (!P.isUndefined(e)) return r(void 0, e, n, i);
	}
	function a(e, t) {
		if (!P.isUndefined(t)) return r(void 0, t);
	}
	function o(e, t) {
		if (!P.isUndefined(t)) return r(void 0, t);
		if (!P.isUndefined(e)) return r(void 0, e);
	}
	function s(n) {
		let r = P.hasOwnProp(t, "transitional") ? t.transitional : void 0;
		if (!P.isUndefined(r)) if (P.isPlainObject(r)) {
			if (P.hasOwnProp(r, n)) return r[n];
		} else return;
		let i = P.hasOwnProp(e, "transitional") ? e.transitional : void 0;
		if (P.isPlainObject(i) && P.hasOwnProp(i, n)) return i[n];
	}
	function c(n, i, a) {
		if (P.hasOwnProp(t, a)) return r(n, i);
		if (P.hasOwnProp(e, a)) return r(void 0, n);
	}
	let l = {
		url: a,
		method: a,
		data: a,
		baseURL: o,
		transformRequest: o,
		transformResponse: o,
		paramsSerializer: o,
		timeout: o,
		timeoutMessage: o,
		withCredentials: o,
		withXSRFToken: o,
		adapter: o,
		responseType: o,
		xsrfCookieName: o,
		xsrfHeaderName: o,
		onUploadProgress: o,
		onDownloadProgress: o,
		decompress: o,
		maxContentLength: o,
		maxBodyLength: o,
		beforeRedirect: o,
		transport: o,
		httpAgent: o,
		httpsAgent: o,
		cancelToken: o,
		socketPath: o,
		allowedSocketPaths: o,
		responseEncoding: o,
		validateStatus: c,
		headers: (e, t, n) => i(pi(e), pi(t), n, !0)
	};
	return P.forEach(Object.keys({
		...e,
		...t
	}), function(r) {
		if (r === "__proto__" || r === "constructor" || r === "prototype") return;
		let a = P.hasOwnProp(l, r) ? l[r] : i, o = a(P.hasOwnProp(e, r) ? e[r] : void 0, P.hasOwnProp(t, r) ? t[r] : void 0, r);
		P.isUndefined(o) && a !== c || (n[r] = o);
	}), P.hasOwnProp(t, "validateStatus") && P.isUndefined(t.validateStatus) && s("validateStatusUndefinedResolves") === !1 && (P.hasOwnProp(e, "validateStatus") ? n.validateStatus = r(void 0, e.validateStatus) : delete n.validateStatus), n;
}
//#endregion
//#region node_modules/axios/lib/helpers/resolveConfig.js
var hi = ["content-type", "content-length"];
function gi(e, t, n) {
	if (n !== "content-only") {
		e.set(t);
		return;
	}
	Object.entries(t || {}).forEach(([t, n]) => {
		hi.includes(t.toLowerCase()) && e.set(t, n);
	});
}
var _i = (e) => encodeURIComponent(e).replace(/%([0-9A-F]{2})/gi, (e, t) => String.fromCharCode(parseInt(t, 16)));
function vi(e) {
	let t = mi({}, e), n = (e) => P.hasOwnProp(t, e) ? t[e] : void 0, r = n("data"), i = n("withXSRFToken"), a = n("xsrfHeaderName"), o = n("xsrfCookieName"), s = n("headers"), c = n("auth"), l = n("baseURL"), u = n("allowAbsoluteUrls"), d = n("url");
	if (t.headers = s = F.from(s), t.url = Or(fi(l, d, u, t), n("params"), n("paramsSerializer")), c) {
		let t = P.getSafeProp(c, "username") || "", n = P.getSafeProp(c, "password") || "";
		try {
			s.set("Authorization", "Basic " + btoa(t + ":" + (n ? _i(n) : "")));
		} catch (t) {
			throw I.from(t, I.ERR_BAD_OPTION_VALUE, e);
		}
	}
	if (P.isFormData(r) && (L.hasStandardBrowserEnv || L.hasStandardBrowserWebWorkerEnv || P.isReactNative(r) ? s.setContentType(void 0) : P.isFunction(r.getHeaders) && gi(s, r.getHeaders(), n("formDataHeaderPolicy"))), L.hasStandardBrowserEnv && (P.isFunction(i) && (i = i(t)), i === !0 || i == null && ri(t.url))) {
		let e = a && o && ii.read(o);
		e && s.set(a, e);
	}
	return t;
}
var yi = typeof XMLHttpRequest < "u" && function(e) {
	return new Promise(function(t, n) {
		let r = vi(e), i = r.data, a = F.from(r.headers).normalize(), { responseType: o, onUploadProgress: s, onDownloadProgress: c } = r, l, u, d, f, p;
		function m() {
			f && f(), p && p(), r.cancelToken && r.cancelToken.unsubscribe(l), r.signal && r.signal.removeEventListener("abort", l);
		}
		let h = new XMLHttpRequest();
		h.open(r.method.toUpperCase(), r.url, !0), h.timeout = r.timeout;
		function g() {
			if (!h) return;
			let r = F.from("getAllResponseHeaders" in h && h.getAllResponseHeaders());
			Xr(function(e) {
				t(e), m();
			}, function(e) {
				n(e), m();
			}, {
				data: !o || o === "text" || o === "json" ? h.responseText : h.response,
				status: h.status,
				statusText: h.statusText,
				headers: r,
				config: e,
				request: h
			}), h = null;
		}
		"onloadend" in h ? h.onloadend = g : h.onreadystatechange = function() {
			!h || h.readyState !== 4 || h.status === 0 && !(h.responseURL && h.responseURL.startsWith("file:")) || setTimeout(g);
		}, h.onabort = function() {
			h &&= (n(new I("Request aborted", I.ECONNABORTED, e, h)), m(), null);
		}, h.onerror = function(t) {
			let r = new I(t && t.message ? t.message : "Network Error", I.ERR_NETWORK, e, h);
			r.event = t || null, n(r), m(), h = null;
		}, h.ontimeout = function() {
			let t = r.timeout ? "timeout of " + r.timeout + "ms exceeded" : "timeout exceeded", i = r.transitional || Ar;
			r.timeoutErrorMessage && (t = r.timeoutErrorMessage), n(new I(t, i.clarifyTimeoutError ? I.ETIMEDOUT : I.ECONNABORTED, e, h)), m(), h = null;
		}, i === void 0 && a.setContentType(null), "setRequestHeader" in h && P.forEach(or(a), function(e, t) {
			h.setRequestHeader(t, e);
		}), P.isUndefined(r.withCredentials) || (h.withCredentials = !!r.withCredentials), o && o !== "json" && (h.responseType = r.responseType), c && ([d, p] = ei(c, !0), h.addEventListener("progress", d)), s && h.upload && ([u, f] = ei(s), h.upload.addEventListener("progress", u), h.upload.addEventListener("loadend", f)), (r.cancelToken || r.signal) && (l = (t) => {
			h &&= (n(!t || t.type ? new Yr(null, e, h) : t), h.abort(), m(), null);
		}, r.cancelToken && r.cancelToken.subscribe(l), r.signal && (r.signal.aborted ? l() : r.signal.addEventListener("abort", l)));
		let _ = Zr(r.url);
		if (_ && !L.protocols.includes(_)) {
			n(new I("Unsupported protocol " + _ + ":", I.ERR_BAD_REQUEST, e)), m();
			return;
		}
		h.send(i || null);
	});
}, bi = (e, t) => {
	if (e = e ? e.filter(Boolean) : [], !t && !e.length) return;
	let n = new AbortController(), r = !1, i = function(e) {
		if (!r) {
			r = !0, o();
			let t = e instanceof Error ? e : this.reason;
			n.abort(t instanceof I ? t : new Yr(t instanceof Error ? t.message : t));
		}
	}, a = t && setTimeout(() => {
		a = null, i(new I(`timeout of ${t}ms exceeded`, I.ETIMEDOUT));
	}, t), o = () => {
		e &&= (a && clearTimeout(a), a = null, e.forEach((e) => {
			e.unsubscribe ? e.unsubscribe(i) : e.removeEventListener("abort", i);
		}), null);
	};
	e.forEach((e) => e.addEventListener("abort", i, { once: !0 }));
	let { signal: s } = n;
	return s.unsubscribe = () => P.asap(o), s;
}, xi = function* (e, t) {
	let n = e.byteLength;
	if (!t || n < t) {
		yield e;
		return;
	}
	let r = 0, i;
	for (; r < n;) i = r + t, yield e.slice(r, i), r = i;
}, Si = async function* (e, t) {
	for await (let n of Ci(e)) yield* xi(n, t);
}, Ci = async function* (e) {
	if (e[Symbol.asyncIterator]) {
		yield* e;
		return;
	}
	let t = e.getReader();
	try {
		for (;;) {
			let { done: e, value: n } = await t.read();
			if (e) break;
			yield n;
		}
	} finally {
		await t.cancel();
	}
}, wi = (e, t, n, r) => {
	let i = Si(e, t), a = 0, o, s = (e) => {
		o || (o = !0, r && r(e));
	};
	return new ReadableStream({
		async pull(e) {
			try {
				let { done: t, value: r } = await i.next();
				if (t) {
					s(), e.close();
					return;
				}
				let o = r.byteLength;
				n && n(a += o), e.enqueue(new Uint8Array(r));
			} catch (e) {
				throw s(e), e;
			}
		},
		cancel(e) {
			return s(e), i.return();
		}
	}, { highWaterMark: 2 });
}, Ti = (e) => e >= 48 && e <= 57 || e >= 65 && e <= 70 || e >= 97 && e <= 102, Ei = (e, t, n) => t + 2 < n && Ti(e.charCodeAt(t + 1)) && Ti(e.charCodeAt(t + 2));
function Di(e) {
	if (!e || typeof e != "string" || !e.startsWith("data:")) return 0;
	let t = e.indexOf(",");
	if (t < 0) return 0;
	let n = e.slice(5, t), r = e.slice(t + 1);
	if (/;base64/i.test(n)) {
		let e = r.length, t = r.length;
		for (let n = 0; n < t; n++) if (r.charCodeAt(n) === 37 && n + 2 < t) {
			let t = r.charCodeAt(n + 1), i = r.charCodeAt(n + 2);
			Ti(t) && Ti(i) && (e -= 2, n += 2);
		}
		let n = 0, i = t - 1, a = (e) => e >= 2 && r.charCodeAt(e - 2) === 37 && r.charCodeAt(e - 1) === 51 && (r.charCodeAt(e) === 68 || r.charCodeAt(e) === 100);
		i >= 0 && (r.charCodeAt(i) === 61 ? (n++, i--) : a(i) && (n++, i -= 3)), n === 1 && i >= 0 && (r.charCodeAt(i) === 61 || a(i)) && n++;
		let o = Math.floor(e / 4) * 3 - (n || 0);
		return o > 0 ? o : 0;
	}
	let i = 0;
	for (let e = 0, t = r.length; e < t; e++) {
		let n = r.charCodeAt(e);
		if (n === 37 && Ei(r, e, t)) i += 1, e += 2;
		else if (n < 128) i += 1;
		else if (n < 2048) i += 2;
		else if (n >= 55296 && n <= 56319 && e + 1 < t) {
			let t = r.charCodeAt(e + 1);
			t >= 56320 && t <= 57343 ? (i += 4, e++) : i += 3;
		} else i += 3;
	}
	return i;
}
//#endregion
//#region node_modules/axios/lib/env/data.js
var Oi = "1.18.1", ki = 64 * 1024, { isFunction: Ai } = P, ji = (e) => encodeURIComponent(e).replace(/%([0-9A-F]{2})/gi, (e, t) => String.fromCharCode(parseInt(t, 16))), Mi = (e) => {
	if (!P.isString(e)) return e;
	try {
		return decodeURIComponent(e);
	} catch {
		return e;
	}
}, Ni = (e, ...t) => {
	try {
		return !!e(...t);
	} catch {
		return !1;
	}
}, Pi = (e) => {
	let t = e.indexOf("://"), n = e;
	return t !== -1 && (n = n.slice(t + 3)), n.includes("@") || n.includes(":");
}, Fi = (e) => {
	let t = P.global !== void 0 && P.global !== null ? P.global : globalThis, { ReadableStream: n, TextEncoder: r } = t;
	e = P.merge.call({ skipUndefined: !0 }, {
		Request: t.Request,
		Response: t.Response
	}, e);
	let { fetch: i, Request: a, Response: o } = e, s = i ? Ai(i) : typeof fetch == "function", c = Ai(a), l = Ai(o);
	if (!s) return !1;
	let u = s && Ai(n), d = s && (typeof r == "function" ? ((e) => (t) => e.encode(t))(new r()) : async (e) => new Uint8Array(await new a(e).arrayBuffer())), f = c && u && Ni(() => {
		let e = !1, t = new a(L.origin, {
			body: new n(),
			method: "POST",
			get duplex() {
				return e = !0, "half";
			}
		}), r = t.headers.has("Content-Type");
		return t.body != null && t.body.cancel(), e && !r;
	}), p = l && u && Ni(() => P.isReadableStream(new o("").body)), m = { stream: p && ((e) => e.body) };
	s && [
		"text",
		"arrayBuffer",
		"blob",
		"formData",
		"stream"
	].forEach((e) => {
		!m[e] && (m[e] = (t, n) => {
			let r = t && t[e];
			if (r) return r.call(t);
			throw new I(`Response type '${e}' is not supported`, I.ERR_NOT_SUPPORT, n);
		});
	});
	let h = async (e) => {
		if (e == null) return 0;
		if (P.isBlob(e)) return e.size;
		if (P.isSpecCompliantForm(e)) return (await new a(L.origin, {
			method: "POST",
			body: e
		}).arrayBuffer()).byteLength;
		if (P.isArrayBufferView(e) || P.isArrayBuffer(e)) return e.byteLength;
		if (P.isURLSearchParams(e) && (e += ""), P.isString(e)) return (await d(e)).byteLength;
	}, g = async (e, t) => P.toFiniteNumber(e.getContentLength()) ?? h(t);
	return async (e) => {
		let { url: t, method: n, data: s, signal: l, cancelToken: d, timeout: _, onDownloadProgress: v, onUploadProgress: y, responseType: b, headers: x, withCredentials: ee = "same-origin", fetchOptions: te, maxContentLength: S, maxBodyLength: ne } = vi(e), C = P.isNumber(S) && S > -1, w = P.isNumber(ne) && ne > -1, re = (t) => P.hasOwnProp(e, t) ? e[t] : void 0, ie = i || fetch;
		b = b ? (b + "").toLowerCase() : "text";
		let T = bi([l, d && d.toAbortSignal()], _), E = null, D = T && T.unsubscribe && (() => {
			T.unsubscribe();
		}), ae, O = null, oe = () => new I("Request body larger than maxBodyLength limit", I.ERR_BAD_REQUEST, e, E);
		try {
			let i, l = re("auth");
			if (l && (i = {
				username: P.getSafeProp(l, "username") || "",
				password: P.getSafeProp(l, "password") || ""
			}), Pi(t)) {
				let e = new URL(t, L.origin);
				!i && (e.username || e.password) && (i = {
					username: Mi(e.username),
					password: Mi(e.password)
				}), (e.username || e.password) && (e.username = "", e.password = "", t = e.href);
			}
			if (i && (x.delete("authorization"), x.set("Authorization", "Basic " + btoa(ji((i.username || "") + ":" + (i.password || ""))))), C && typeof t == "string" && t.startsWith("data:") && Di(t) > S) throw new I("maxContentLength size of " + S + " exceeded", I.ERR_BAD_RESPONSE, e, E);
			if (w && n !== "get" && n !== "head") {
				let e = await h(s);
				if (typeof e == "number" && isFinite(e) && (ae = e, e > ne)) throw oe();
			}
			let d = w && (P.isReadableStream(s) || P.isStream(s)), _ = (e, t, n) => wi(e, ki, (e) => {
				if (w && e > ne) throw O = oe();
				t && t(e);
			}, n);
			if (f && n !== "get" && n !== "head" && (y || d)) {
				if (ae ??= await g(x, s), ae !== 0 || d) {
					let e = new a(t, {
						method: "POST",
						body: s,
						duplex: "half"
					}), n;
					if (P.isFormData(s) && (n = e.headers.get("content-type")) && x.setContentType(n), e.body) {
						let [t, n] = y && ti(ae, ei(ni(y))) || [];
						s = _(e.body, t, n);
					}
				}
			} else if (d && !c && u && n !== "get" && n !== "head") s = _(s);
			else if (d && c && !f && n !== "get" && n !== "head") throw new I("Stream request bodies are not supported by the current fetch implementation", I.ERR_NOT_SUPPORT, e, E);
			P.isString(ee) || (ee = ee ? "include" : "omit");
			let se = c && "credentials" in a.prototype;
			if (P.isFormData(s)) {
				let e = x.getContentType();
				e && /^multipart\/form-data/i.test(e) && !/boundary=/i.test(e) && x.delete("content-type");
			}
			x.set("User-Agent", "axios/" + Oi, !1);
			let ce = {
				...te,
				signal: T,
				method: n.toUpperCase(),
				headers: or(x.normalize()),
				body: s,
				duplex: "half",
				credentials: se ? ee : void 0
			};
			E = c && new a(t, ce);
			let k = await (c ? ie(E, te) : ie(t, ce)), le = F.from(k.headers);
			if (C) {
				let t = P.toFiniteNumber(le.getContentLength());
				if (t != null && t > S) throw new I("maxContentLength size of " + S + " exceeded", I.ERR_BAD_RESPONSE, e, E);
			}
			let ue = p && (b === "stream" || b === "response");
			if (p && k.body && (v || C || ue && D)) {
				let t = {};
				[
					"status",
					"statusText",
					"headers"
				].forEach((e) => {
					t[e] = k[e];
				});
				let n = P.toFiniteNumber(le.getContentLength()), [r, i] = v && ti(n, ei(ni(v), !0)) || [], a = 0;
				k = new o(wi(k.body, ki, (t) => {
					if (C && (a = t, a > S)) throw new I("maxContentLength size of " + S + " exceeded", I.ERR_BAD_RESPONSE, e, E);
					r && r(t);
				}, () => {
					i && i(), D && D();
				}), t);
			}
			b ||= "text";
			let A = await m[P.findKey(m, b) || "text"](k, e);
			if (C && !p && !ue) {
				let t;
				if (A != null && (typeof A.byteLength == "number" ? t = A.byteLength : typeof A.size == "number" ? t = A.size : typeof A == "string" && (t = typeof r == "function" ? new r().encode(A).byteLength : A.length)), typeof t == "number" && t > S) throw new I("maxContentLength size of " + S + " exceeded", I.ERR_BAD_RESPONSE, e, E);
			}
			return !ue && D && D(), await new Promise((t, n) => {
				Xr(t, n, {
					data: A,
					headers: F.from(k.headers),
					status: k.status,
					statusText: k.statusText,
					config: e,
					request: E
				});
			});
		} catch (t) {
			if (D && D(), T && T.aborted && T.reason instanceof I) {
				let n = T.reason;
				throw n.config = e, E && (n.request = E), t !== n && Object.defineProperty(n, "cause", {
					__proto__: null,
					value: t,
					writable: !0,
					enumerable: !1,
					configurable: !0
				}), n;
			}
			if (O) throw E && !O.request && (O.request = E), O;
			if (t instanceof I) throw E && !t.request && (t.request = E), t;
			if (t && t.name === "TypeError" && /Load failed|fetch/i.test(t.message)) {
				let n = new I("Network Error", I.ERR_NETWORK, e, E, t && t.response);
				throw Object.defineProperty(n, "cause", {
					__proto__: null,
					value: t.cause || t,
					writable: !0,
					enumerable: !1,
					configurable: !0
				}), n;
			}
			throw I.from(t, t && t.code, e, E, t && t.response);
		}
	};
}, Ii = /* @__PURE__ */ new Map(), Li = (e) => {
	let t = e && e.env || {}, { fetch: n, Request: r, Response: i } = t, a = [
		r,
		i,
		n
	], o = a.length, s, c, l = Ii;
	for (; o--;) s = a[o], c = l.get(s), c === void 0 && l.set(s, c = o ? /* @__PURE__ */ new Map() : Fi(t)), l = c;
	return c;
};
Li();
//#endregion
//#region node_modules/axios/lib/adapters/adapters.js
var Ri = {
	http: null,
	xhr: yi,
	fetch: { get: Li }
};
P.forEach(Ri, (e, t) => {
	if (e) {
		try {
			Object.defineProperty(e, "name", {
				__proto__: null,
				value: t
			});
		} catch {}
		Object.defineProperty(e, "adapterName", {
			__proto__: null,
			value: t
		});
	}
});
var zi = (e) => `- ${e}`, Bi = (e) => P.isFunction(e) || e === null || e === !1;
function Vi(e, t) {
	e = P.isArray(e) ? e : [e];
	let { length: n } = e, r, i, a = {};
	for (let o = 0; o < n; o++) {
		r = e[o];
		let n;
		if (i = r, !Bi(r) && (i = Ri[(n = String(r)).toLowerCase()], i === void 0)) throw new I(`Unknown adapter '${n}'`);
		if (i && (P.isFunction(i) || (i = i.get(t)))) break;
		a[n || "#" + o] = i;
	}
	if (!i) {
		let e = Object.entries(a).map(([e, t]) => `adapter ${e} ` + (t === !1 ? "is not supported by the environment" : "is not available in the build"));
		throw new I("There is no suitable adapter to dispatch the request " + (n ? e.length > 1 ? "since :\n" + e.map(zi).join("\n") : " " + zi(e[0]) : "as no adapter specified"), I.ERR_NOT_SUPPORT);
	}
	return i;
}
var Hi = {
	getAdapter: Vi,
	adapters: Ri
};
//#endregion
//#region node_modules/axios/lib/core/dispatchRequest.js
function Ui(e) {
	if (e.cancelToken && e.cancelToken.throwIfRequested(), e.signal && e.signal.aborted) throw new Yr(null, e);
}
function Wi(e) {
	return Ui(e), e.headers = F.from(e.headers), e.data = qr.call(e, e.transformRequest), [
		"post",
		"put",
		"patch"
	].indexOf(e.method) !== -1 && e.headers.setContentType("application/x-www-form-urlencoded", !1), Hi.getAdapter(e.adapter || Kr.adapter, e)(e).then(function(t) {
		Ui(e), e.response = t;
		try {
			t.data = qr.call(e, e.transformResponse, t);
		} finally {
			delete e.response;
		}
		return t.headers = F.from(t.headers), t;
	}, function(t) {
		if (!Jr(t) && (Ui(e), t && t.response)) {
			e.response = t.response;
			try {
				t.response.data = qr.call(e, e.transformResponse, t.response);
			} finally {
				delete e.response;
			}
			t.response.headers = F.from(t.response.headers);
		}
		return Promise.reject(t);
	});
}
//#endregion
//#region node_modules/axios/lib/helpers/validator.js
var Gi = {};
[
	"object",
	"boolean",
	"number",
	"function",
	"string",
	"symbol"
].forEach((e, t) => {
	Gi[e] = function(n) {
		return typeof n === e || "a" + (t < 1 ? "n " : " ") + e;
	};
});
var Ki = {};
Gi.transitional = function(e, t, n) {
	function r(e, t) {
		return "[Axios v" + Oi + "] Transitional option '" + e + "'" + t + (n ? ". " + n : "");
	}
	return (n, i, a) => {
		if (e === !1) throw new I(r(i, " has been removed" + (t ? " in " + t : "")), I.ERR_DEPRECATED);
		return t && !Ki[i] && (Ki[i] = !0, console.warn(r(i, " has been deprecated since v" + t + " and will be removed in the near future"))), e ? e(n, i, a) : !0;
	};
}, Gi.spelling = function(e) {
	return (t, n) => (console.warn(`${n} is likely a misspelling of ${e}`), !0);
};
function qi(e, t, n) {
	if (typeof e != "object" || !e) throw new I("options must be an object", I.ERR_BAD_OPTION_VALUE);
	let r = Object.keys(e), i = r.length;
	for (; i-- > 0;) {
		let a = r[i], o = Object.prototype.hasOwnProperty.call(t, a) ? t[a] : void 0;
		if (o) {
			let t = e[a], n = t === void 0 || o(t, a, e);
			if (n !== !0) throw new I("option " + a + " must be " + n, I.ERR_BAD_OPTION_VALUE);
			continue;
		}
		if (n !== !0) throw new I("Unknown option " + a, I.ERR_BAD_OPTION);
	}
}
var Ji = {
	assertOptions: qi,
	validators: Gi
}, R = Ji.validators, Yi = class {
	constructor(e) {
		this.defaults = e || {}, this.interceptors = {
			request: new kr(),
			response: new kr()
		};
	}
	async request(e, t) {
		try {
			return await this._request(e, t);
		} catch (e) {
			if (e instanceof Error) {
				let t = {};
				Error.captureStackTrace ? Error.captureStackTrace(t) : t = /* @__PURE__ */ Error();
				let n = (() => {
					if (!t.stack) return "";
					let e = t.stack.indexOf("\n");
					return e === -1 ? "" : t.stack.slice(e + 1);
				})();
				try {
					if (!e.stack) e.stack = n;
					else if (n) {
						let t = n.indexOf("\n"), r = t === -1 ? -1 : n.indexOf("\n", t + 1), i = r === -1 ? "" : n.slice(r + 1);
						String(e.stack).endsWith(i) || (e.stack += "\n" + n);
					}
				} catch {}
			}
			throw e;
		}
	}
	_request(e, t) {
		typeof e == "string" ? (t ||= {}, t.url = e) : t = e || {}, t = mi(this.defaults, t);
		let { transitional: n, paramsSerializer: r, headers: i } = t;
		n !== void 0 && Ji.assertOptions(n, {
			silentJSONParsing: R.transitional(R.boolean),
			forcedJSONParsing: R.transitional(R.boolean),
			clarifyTimeoutError: R.transitional(R.boolean),
			legacyInterceptorReqResOrdering: R.transitional(R.boolean),
			advertiseZstdAcceptEncoding: R.transitional(R.boolean),
			validateStatusUndefinedResolves: R.transitional(R.boolean)
		}, !1), r != null && (P.isFunction(r) ? t.paramsSerializer = { serialize: r } : Ji.assertOptions(r, {
			encode: R.function,
			serialize: R.function
		}, !0)), t.allowAbsoluteUrls !== void 0 || (this.defaults.allowAbsoluteUrls === void 0 ? t.allowAbsoluteUrls = !0 : t.allowAbsoluteUrls = this.defaults.allowAbsoluteUrls), Ji.assertOptions(t, {
			baseUrl: R.spelling("baseURL"),
			withXsrfToken: R.spelling("withXSRFToken")
		}, !0), t.method = (t.method || this.defaults.method || "get").toLowerCase();
		let a = i && P.merge(i.common, i[t.method]);
		i && P.forEach([
			"delete",
			"get",
			"head",
			"post",
			"put",
			"patch",
			"query",
			"common"
		], (e) => {
			delete i[e];
		}), t.headers = F.concat(a, i);
		let o = [], s = !0;
		this.interceptors.request.forEach(function(e) {
			if (typeof e.runWhen == "function" && e.runWhen(t) === !1) return;
			s &&= e.synchronous;
			let n = t.transitional || Ar;
			n && n.legacyInterceptorReqResOrdering ? o.unshift(e.fulfilled, e.rejected) : o.push(e.fulfilled, e.rejected);
		});
		let c = [];
		this.interceptors.response.forEach(function(e) {
			c.push(e.fulfilled, e.rejected);
		});
		let l, u = 0, d;
		if (!s) {
			let e = [Wi.bind(this), void 0];
			for (e.unshift(...o), e.push(...c), d = e.length, l = Promise.resolve(t); u < d;) l = l.then(e[u++], e[u++]);
			return l;
		}
		d = o.length;
		let f = t;
		for (; u < d;) {
			let e = o[u++], t = o[u++];
			try {
				f = e(f);
			} catch (e) {
				t.call(this, e);
				break;
			}
		}
		try {
			l = Wi.call(this, f);
		} catch (e) {
			return Promise.reject(e);
		}
		for (u = 0, d = c.length; u < d;) l = l.then(c[u++], c[u++]);
		return l;
	}
	getUri(e) {
		return e = mi(this.defaults, e), Or(fi(e.baseURL, e.url, e.allowAbsoluteUrls, e), e.params, e.paramsSerializer);
	}
};
P.forEach([
	"delete",
	"get",
	"head",
	"options"
], function(e) {
	Yi.prototype[e] = function(t, n) {
		return this.request(mi(n || {}, {
			method: e,
			url: t,
			data: n && P.hasOwnProp(n, "data") ? n.data : void 0
		}));
	};
}), P.forEach([
	"post",
	"put",
	"patch",
	"query"
], function(e) {
	function t(t) {
		return function(n, r, i) {
			return this.request(mi(i || {}, {
				method: e,
				headers: t ? { "Content-Type": "multipart/form-data" } : {},
				url: n,
				data: r
			}));
		};
	}
	Yi.prototype[e] = t(), e !== "query" && (Yi.prototype[e + "Form"] = t(!0));
});
//#endregion
//#region node_modules/axios/lib/cancel/CancelToken.js
var Xi = class e {
	constructor(e) {
		if (typeof e != "function") throw TypeError("executor must be a function.");
		let t;
		this.promise = new Promise(function(e) {
			t = e;
		});
		let n = this;
		this.promise.then((e) => {
			if (!n._listeners) return;
			let t = n._listeners.length;
			for (; t-- > 0;) n._listeners[t](e);
			n._listeners = null;
		}), this.promise.then = (e) => {
			let t, r = new Promise((e) => {
				n.subscribe(e), t = e;
			}).then(e);
			return r.cancel = function() {
				n.unsubscribe(t);
			}, r;
		}, e(function(e, r, i) {
			n.reason || (n.reason = new Yr(e, r, i), t(n.reason));
		});
	}
	throwIfRequested() {
		if (this.reason) throw this.reason;
	}
	subscribe(e) {
		if (this.reason) {
			e(this.reason);
			return;
		}
		this._listeners ? this._listeners.push(e) : this._listeners = [e];
	}
	unsubscribe(e) {
		if (!this._listeners) return;
		let t = this._listeners.indexOf(e);
		t !== -1 && this._listeners.splice(t, 1);
	}
	toAbortSignal() {
		let e = new AbortController(), t = (t) => {
			e.abort(t);
		};
		return this.subscribe(t), e.signal.unsubscribe = () => this.unsubscribe(t), e.signal;
	}
	static source() {
		let t;
		return {
			token: new e(function(e) {
				t = e;
			}),
			cancel: t
		};
	}
};
//#endregion
//#region node_modules/axios/lib/helpers/spread.js
function Zi(e) {
	return function(t) {
		return e.apply(null, t);
	};
}
//#endregion
//#region node_modules/axios/lib/helpers/isAxiosError.js
function Qi(e) {
	return P.isObject(e) && e.isAxiosError === !0;
}
//#endregion
//#region node_modules/axios/lib/helpers/HttpStatusCode.js
var $i = {
	Continue: 100,
	SwitchingProtocols: 101,
	Processing: 102,
	EarlyHints: 103,
	Ok: 200,
	Created: 201,
	Accepted: 202,
	NonAuthoritativeInformation: 203,
	NoContent: 204,
	ResetContent: 205,
	PartialContent: 206,
	MultiStatus: 207,
	AlreadyReported: 208,
	ImUsed: 226,
	MultipleChoices: 300,
	MovedPermanently: 301,
	Found: 302,
	SeeOther: 303,
	NotModified: 304,
	UseProxy: 305,
	Unused: 306,
	TemporaryRedirect: 307,
	PermanentRedirect: 308,
	BadRequest: 400,
	Unauthorized: 401,
	PaymentRequired: 402,
	Forbidden: 403,
	NotFound: 404,
	MethodNotAllowed: 405,
	NotAcceptable: 406,
	ProxyAuthenticationRequired: 407,
	RequestTimeout: 408,
	Conflict: 409,
	Gone: 410,
	LengthRequired: 411,
	PreconditionFailed: 412,
	PayloadTooLarge: 413,
	UriTooLong: 414,
	UnsupportedMediaType: 415,
	RangeNotSatisfiable: 416,
	ExpectationFailed: 417,
	ImATeapot: 418,
	MisdirectedRequest: 421,
	UnprocessableEntity: 422,
	Locked: 423,
	FailedDependency: 424,
	TooEarly: 425,
	UpgradeRequired: 426,
	PreconditionRequired: 428,
	TooManyRequests: 429,
	RequestHeaderFieldsTooLarge: 431,
	UnavailableForLegalReasons: 451,
	InternalServerError: 500,
	NotImplemented: 501,
	BadGateway: 502,
	ServiceUnavailable: 503,
	GatewayTimeout: 504,
	HttpVersionNotSupported: 505,
	VariantAlsoNegotiates: 506,
	InsufficientStorage: 507,
	LoopDetected: 508,
	NotExtended: 510,
	NetworkAuthenticationRequired: 511,
	WebServerIsDown: 521,
	ConnectionTimedOut: 522,
	OriginIsUnreachable: 523,
	TimeoutOccurred: 524,
	SslHandshakeFailed: 525,
	InvalidSslCertificate: 526
};
Object.entries($i).forEach(([e, t]) => {
	$i[t] = e;
});
//#endregion
//#region node_modules/axios/lib/axios.js
function ea(e) {
	let t = new Yi(e), n = Lt(Yi.prototype.request, t);
	return P.extend(n, Yi.prototype, t, { allOwnKeys: !0 }), P.extend(n, t, null, { allOwnKeys: !0 }), n.create = function(t) {
		return ea(mi(e, t));
	}, n;
}
var z = ea(Kr);
z.Axios = Yi, z.CanceledError = Yr, z.CancelToken = Xi, z.isCancel = Jr, z.VERSION = Oi, z.toFormData = Cr, z.AxiosError = I, z.Cancel = z.CanceledError, z.all = function(e) {
	return Promise.all(e);
}, z.spread = Zi, z.isAxiosError = Qi, z.mergeConfig = mi, z.AxiosHeaders = F, z.formToJSON = (e) => Ur(P.isHTMLForm(e) ? new FormData(e) : e), z.getAdapter = Hi.getAdapter, z.HttpStatusCode = $i, z.default = z;
//#endregion
//#region \0plugin-vue:export-helper
var ta = (e, t) => {
	let n = e.__vccOpts || e;
	for (let [e, r] of t) n[e] = r;
	return n;
}, na = {
	created() {
		this.getWeek(0);
	},
	methods: {
		formatDate(e) {
			let t = window.Quasar.date.extractDate(e.substring(0, 16), "YYYY-MM-DDTHH:mm");
			return window.Quasar.date.formatDate(t, "DD/MM/YYYY");
		},
		updateData() {
			this.loading = !0, this.fail = !1, this.data = [], z.get(`${this.apiUrl}/executions/summaries?weekOffset=${this.offset}&status=${this.status}`).then((e) => {
				e.data.map((e) => {
					e.lastExecutionTime = e.lastExecutionTime ? this.formatDate(e.lastExecutionTime) : "-", e.nextExecutionTime = e.nextExecutionTime ? this.formatDate(e.nextExecutionTime) : "-", this.data.push(e);
				}), this.loading = !1;
			}).catch((e) => {
				this.fail = !0, this.loading = !1, console.error(e);
			});
		},
		getWeekLimits: function(e) {
			let t = window.Quasar.date.addToDate(Date.now(), { days: e * 7 }), n = window.Quasar.date.getDayOfWeek(Date.now()), r = window.Quasar.date.subtractFromDate(t, { days: n ? n - 1 : 6 }), i = window.Quasar.date.addToDate(t, { days: n ? 7 - n : 0 });
			return {
				startOfWeek: window.Quasar.date.formatDate(r, "DD/MM/YYYY"),
				endOfWeek: window.Quasar.date.formatDate(i, "DD/MM/YYYY")
			};
		},
		getWeek(e) {
			this.offset = e ? this.offset + e : 0;
			let t = this.getWeekLimits(this.offset);
			this.startOfWeek = t.startOfWeek, this.endOfWeek = t.endOfWeek, this.status ||= "A", this.updateData();
		},
		getStatus(e) {
			this.status = e, this.updateData();
		}
	},
	props: ["apiUrl"],
	data() {
		return {
			columns: [
				{
					name: "processLabel",
					label: this.$vui.i18n().vuiOrchestra.orchestra.processLabel,
					align: "left",
					field: "processLabel"
				},
				{
					name: "state",
					label: this.$vui.i18n().vuiOrchestra.orchestra.state,
					field: "state",
					align: "center"
				},
				{
					name: "lastExecutionTime",
					label: this.$vui.i18n().vuiOrchestra.orchestra.lastExecutionTime,
					field: "lastExecutionTime"
				},
				{
					name: "nextExecutionTime",
					label: this.$vui.i18n().vuiOrchestra.orchestra.nextExecutionTime,
					field: "nextExecutionTime"
				}
			],
			data: [],
			loading: !1,
			fail: !1,
			offset: 0,
			status: "A",
			tab: "all",
			startOfWeek: "",
			endOfWeek: ""
		};
	},
	watch: { "$q.lang": function() {
		this.columns = this.columns.map((e) => ({
			...e,
			label: this.$vui.i18n().vuiOrchestra.orchestra[e.name]
		}));
	} }
}, B = window.Vue.toDisplayString, V = window.Vue.createElementVNode, ra = window.Vue.resolveComponent, H = window.Vue.createVNode, ia = window.Vue.withCtx, aa = window.Vue.createTextVNode, oa = window.Vue.openBlock, sa = window.Vue.createElementBlock;
window.Vue.createCommentVNode;
var ca = { class: "row justify-center" }, la = { style: { width: "1300px" } }, ua = { class: "text-h5 row q-mt-lg" }, da = { class: "q-mx-auto" }, fa = { class: "row q-mt-lg" }, pa = { class: "row q-mt-lg" }, ma = { class: "q-mx-auto q-gutter-md" }, ha = { class: "q-pa-md" }, ga = { class: "row q-col-gutter-x-md" }, _a = { class: "col text-center" }, va = { class: "col text-center" }, ya = { class: "col text-center" }, ba = { class: "row q-col-gutter-x-md" }, xa = { class: "col text-center" }, Sa = { class: "col text-center" }, Ca = { class: "col text-center" }, wa = { class: "full-width row flex-center q-gutter-sm" }, Ta = { key: 0 }, Ea = { key: 1 }, Da = { key: 2 };
function Oa(e, t, n, r, i, a) {
	let o = ra("q-tab"), s = ra("q-tabs"), c = ra("q-btn"), l = ra("q-td"), u = ra("q-icon"), d = ra("q-tr"), f = ra("q-spinner"), p = ra("q-table");
	return oa(), sa("div", ca, [V("div", la, [
		V("div", ua, [V("div", da, B(e.$vui.i18n().vuiOrchestra.orchestra.title.part1) + " " + B(i.startOfWeek) + " " + B(e.$vui.i18n().vuiOrchestra.orchestra.title.part2) + " " + B(i.endOfWeek), 1)]),
		V("div", fa, [H(s, {
			modelValue: i.tab,
			"onUpdate:modelValue": t[4] ||= (e) => i.tab = e,
			"inline-label": "",
			class: "text-primary q-mx-auto"
		}, {
			default: ia(() => [
				H(o, {
					name: "all",
					icon: "list",
					label: e.$vui.i18n().vuiOrchestra.orchestra.all,
					onClick: t[0] ||= (e) => a.getStatus("A")
				}, null, 8, ["label"]),
				H(o, {
					name: "success",
					icon: "done",
					label: e.$vui.i18n().vuiOrchestra.orchestra.success,
					onClick: t[1] ||= (e) => a.getStatus("SUCCESS"),
					class: "text-green"
				}, null, 8, ["label"]),
				H(o, {
					name: "error",
					icon: "error",
					label: e.$vui.i18n().vuiOrchestra.orchestra.error,
					onClick: t[2] ||= (e) => a.getStatus("ERROR"),
					class: "text-red"
				}, null, 8, ["label"]),
				H(o, {
					name: "misfired",
					icon: "timer_off",
					label: e.$vui.i18n().vuiOrchestra.orchestra.misfired,
					onClick: t[3] ||= (e) => a.getStatus("MISFIRED"),
					class: "text-grey"
				}, null, 8, ["label"])
			]),
			_: 1
		}, 8, ["modelValue"])]),
		V("div", pa, [V("div", ma, [
			H(c, {
				color: "primary",
				icon: "navigate_before",
				label: e.$vui.i18n().vuiOrchestra.orchestra.previousWeek,
				onClick: t[5] ||= (e) => a.getWeek(-1)
			}, null, 8, ["label"]),
			H(c, {
				round: "",
				color: "primary",
				icon: "today",
				onClick: t[6] ||= (e) => a.getWeek(0)
			}),
			H(c, {
				color: "primary",
				"icon-right": "navigate_next",
				label: e.$vui.i18n().vuiOrchestra.orchestra.nextWeek,
				onClick: t[7] ||= (e) => a.getWeek(1)
			}, null, 8, ["label"])
		])]),
		V("div", ha, [H(p, {
			rows: i.data,
			columns: i.columns,
			"row-key": "name",
			loading: i.loading
		}, {
			body: ia((t) => [H(d, {
				props: t,
				onClick: (n) => e.$router.push("/process/" + t.row.processName)
			}, {
				default: ia(() => [
					H(l, {
						key: "processLabel",
						props: t
					}, {
						default: ia(() => [aa(B(t.row.processLabel), 1)]),
						_: 2
					}, 1032, ["props"]),
					H(l, {
						key: "state",
						props: t
					}, {
						default: ia(() => [V("div", ga, [
							V("div", _a, [H(u, {
								name: "done",
								size: "sm",
								class: "text-green"
							})]),
							V("div", va, [H(u, {
								name: "error",
								size: "sm",
								class: "text-red"
							})]),
							V("div", ya, [H(u, {
								name: "timer_off",
								size: "sm",
								class: "text-grey"
							})])
						]), V("div", ba, [
							V("div", xa, B(t.row.successfulCount), 1),
							V("div", Sa, B(t.row.errorsCount), 1),
							V("div", Ca, B(t.row.misfiredCount), 1)
						])]),
						_: 2
					}, 1032, ["props"]),
					H(l, {
						key: "lastExecutionTime",
						props: t
					}, {
						default: ia(() => [aa(B(t.row.lastExecutionTime), 1)]),
						_: 2
					}, 1032, ["props"]),
					H(l, {
						key: "nextExecutionTime",
						props: t
					}, {
						default: ia(() => [aa(B(t.row.nextExecutionTime), 1)]),
						_: 2
					}, 1032, ["props"])
				]),
				_: 2
			}, 1032, ["props", "onClick"])]),
			"no-data": ia(() => [V("div", wa, [i.loading ? (oa(), sa("span", Ta, [aa(B(e.$vui.i18n().vuiOrchestra.orchestra.loading) + " ", 1), H(f)])) : i.fail ? (oa(), sa("span", Ea, B(e.$vui.i18n().vuiOrchestra.orchestra.connectionFailed), 1)) : (oa(), sa("span", Da, B(e.$vui.i18n().vuiOrchestra.orchestra.noData), 1))])]),
			_: 1
		}, 8, [
			"rows",
			"columns",
			"loading"
		])])
	])]);
}
var ka = /*#__PURE__*/ ta(na, [["render", Oa]]), Aa = {
	created() {
		z.get(`${this.apiUrl}/definitions/${this.$props.processName}`).then((e) => {
			this.processInfo = e.data, this.form.technical = {
				cronExpression: this.processInfo.triggeringStrategy ? this.processInfo.triggeringStrategy.cronExpression : "",
				active: Object.entries(this.processInfo).length ? this.processInfo.active ? "true" : "false" : "-",
				multiExecution: this.processInfo.triggeringStrategy ? this.processInfo.triggeringStrategy.multiExecution ? "true" : "false" : "-",
				rescuePeriod: this.processInfo.triggeringStrategy ? this.processInfo.triggeringStrategy.rescuePeriodInSeconds : ""
			}, this.form.settings = this.processInfo.triggeringStrategy && this.processInfo.triggeringStrategy.initialParams ? { ...this.processInfo.triggeringStrategy.initialParams } : {};
		}).catch((e) => {
			console.error(e), e.response ? this.errorMessage = `${this.$vui.i18n().vuiOrchestra.orchestra.noProcess}: ${this.$props.processName}` : this.errorMessage = this.$vui.i18n().vuiOrchestra.orchestra.connectionFailed, this.connectionFailure = !e.response;
		}), z.get(`${this.apiUrl}/executions/summaries/${this.$props.processName}`).then((e) => {
			this.processSummary = e.data;
		}).catch((e) => {
			console.error(e);
		});
	},
	props: [
		"apiUrl",
		"orchestraUiReadOnly",
		"processName"
	],
	data() {
		return {
			editMode: {
				technical: !1,
				settings: !1
			},
			executionsLoaded: !1,
			limit: 20,
			offset: 0,
			connectionFailure: "?",
			errorMessage: "",
			status: "",
			processInfo: {},
			processSummary: {},
			filterTab: "all",
			executions: [],
			activities: {},
			expandedExecutions: {},
			form: {
				technical: {},
				settings: {}
			},
			tabs: {},
			splitterModel: 50,
			splitterModelExecutions: 20
		};
	},
	watch: { "$q.lang": function() {
		this.errorMessage && (this.connectionFailure == 0 ? this.errorMessage = `${this.$vui.i18n().vuiOrchestra.orchestra.noProcess}: ${this.$props.processName}` : this.connectionFailure == 1 && (this.errorMessage = this.$vui.i18n().vuiOrchestra.orchestra.connectionFailed));
	} },
	methods: {
		getIconFromExecutionState(e) {
			switch (e) {
				case "DONE": return "done";
				case "ERROR": return "error";
				case "ABORTED": return "flash_on";
				default: return "help";
			}
		},
		getColorFromExecutionState(e) {
			switch (e) {
				case "DONE": return "green";
				case "ERROR": return "red";
				case "ABORTED": return "orange";
				default: return "grey";
			}
		},
		formatDate(e) {
			if (e) {
				let t = window.Quasar.date.extractDate(e.substring(0, 16), "YYYY-MM-DDTHH:mm");
				return window.Quasar.date.formatDate(t, "DD/MM/YYYY HH:mm");
			}
			return "";
		},
		executeNow: function(e) {
			z.post(`${this.apiUrl}/executionsControl/executeNow`, { processName: e }).then((e) => {
				this.$q.notify({
					message: this.$vui.i18n().vuiOrchestra.orchestra.executeNowOk,
					color: "positive"
				});
			}).catch((e) => {
				console.error(e), this.$q.notify({
					message: this.$vui.i18n().vuiOrchestra.orchestra.executeNowErr,
					color: "negative"
				});
			});
		},
		fetchActivities: function(e) {
			z.get(`${this.apiUrl}/executions/${e}/activities`).then((t) => {
				this.activities[e] = t.data;
			}).catch((e) => {
				console.error(e);
			});
		},
		updateExecutions: function(e) {
			this.limit = 20, this.offset = 0, this.status = e, this.errorMessage = "", this.connectionFailure = "?", z.get(`${this.apiUrl}/executions/?processName=${this.$props.processName}&status=${this.status}&limit=${this.limit}`).then((e) => {
				this.executions = e.data, e.data.map((e) => {
					this.tabs[e.preId] = "info";
				});
			}).catch((e) => {
				console.error(e), e.response ? this.errorMessage = `${this.$vui.i18n().vuiOrchestra.orchestra.noProcess}: ${this.$props.processName}` : this.errorMessage = this.$vui.i18n().vuiOrchestra.orchestra.connectionFailed, this.connectionFailure = !e.response;
			});
		},
		onLoad(e, t) {
			z.get(`${this.apiUrl}/executions/?processName=${this.$props.processName}&status=${this.status}&limit=${this.limit}&offset=${this.offset}`).then((e) => {
				this.executions.push(...e.data), e.data.map((e) => {
					this.tabs[e.preId] = "info";
				}), e.data.length < this.limit && (this.executionsLoaded = !0, t(!0)), t(!1);
			}), this.offset += 20;
		},
		onSubmit(e) {
			e == "technical" ? z.put(`${this.apiUrl}/definitions/${this.$props.processName}/properties`, {
				cronExpression: this.form.technical.cronExpression,
				multiExecution: this.form.technical.multiExecution == "true",
				rescuePeriod: this.form.technical.rescuePeriod,
				active: this.form.technical.active == "true"
			}).then(this.$router.go()).catch(console.error) : e == "settings" && z.put(`${this.apiUrl}/definitions/${this.$props.processName}/params`, { initialParams: this.form.settings }).then(this.$router.go()).catch(console.error);
		}
	}
}, U = window.Vue.toDisplayString, W = window.Vue.openBlock, G = window.Vue.createElementBlock, K = window.Vue.createCommentVNode, q = window.Vue.resolveComponent, J = window.Vue.createVNode, Y = window.Vue.createElementVNode, X = window.Vue.withCtx, Z = window.Vue.createTextVNode, ja = window.Vue.createBlock, Ma = window.Vue.renderList, Na = window.Vue.Fragment, Pa = { class: "q-pa-md text-primary" }, Fa = { class: "text-h4 q-mb-md" }, Ia = { key: 0 }, La = { key: 1 }, Ra = { class: "text-h6" }, za = { class: "col" }, Ba = { class: "text-weight-medium" }, Va = { class: "col" }, Ha = { class: "text-weight-medium" }, Ua = { class: "col" }, Wa = { class: "text-weight-medium" }, Ga = { class: "text-h6" }, Ka = { class: "text-h6" }, qa = { key: 0 }, Ja = { class: "q-gutter-sm" }, Ya = { class: "q-gutter-sm" }, Xa = { key: 1 }, Za = { class: "text-h6" }, Qa = { key: 0 }, $a = { key: 0 }, eo = { key: 1 }, to = { class: "q-pa-md" }, no = { class: "text-h4 q-gutter-x-md q-mx-auto row" }, ro = { class: "col" }, io = { class: "col-auto" }, ao = { class: "text-h5" }, oo = {
	style: { "max-width": "400px" },
	class: "text-primary"
}, so = { class: "text-weight-medium" }, co = { class: "text-weight-medium" }, lo = { class: "text-weight-medium" }, uo = { class: "text-h5" }, fo = { style: { "max-width": "400px" } }, po = { class: "text-weight-medium" }, mo = { class: "text-weight-medium" }, ho = { class: "text-weight-medium" }, go = { class: "text-weight-medium" }, _o = { class: "text-weight-medium" }, vo = { class: "row justify-center q-my-md" }, yo = { key: 0 }, bo = {
	key: 1,
	class: "text-weight-medium"
};
function xo(e, t, n, r, i, a) {
	let o = q("q-spinner-ios"), s = q("q-card-section"), c = q("q-separator"), l = q("q-icon"), u = q("q-item-section"), d = q("q-item"), f = q("q-list"), p = q("q-btn"), m = q("q-card"), h = q("q-input"), g = q("q-radio"), _ = q("q-form"), v = q("q-tab"), y = q("q-tabs"), b = q("q-avatar"), x = q("q-tooltip"), ee = q("q-tab-panel"), te = q("q-expansion-item"), S = q("q-tab-panels"), ne = q("q-splitter"), C = q("q-infinite-scroll");
	return W(), ja(ne, {
		modelValue: i.splitterModel,
		"onUpdate:modelValue": t[16] ||= (e) => i.splitterModel = e,
		style: { height: "85vh" },
		limits: [0, 100]
	}, {
		before: X(() => [Y("div", Pa, [
			Y("div", Fa, [i.processInfo.label || i.errorMessage ? (W(), G("div", Ia, U(i.processInfo.label || i.errorMessage), 1)) : (W(), G("div", La, [J(o, {
				color: "primary",
				size: "sm"
			})]))]),
			J(m, { class: "my-card" }, {
				default: X(() => [
					J(s, null, {
						default: X(() => [Y("div", Ra, U(e.$vui.i18n().vuiOrchestra.orchestra.totalExecutions), 1)]),
						_: 1
					}),
					J(c, { inset: "" }),
					J(s, { class: "row" }, {
						default: X(() => [
							Y("div", za, [J(f, null, {
								default: X(() => [J(d, null, {
									default: X(() => [J(u, null, {
										default: X(() => [Y("div", Ba, [J(l, {
											color: "green",
											name: "done",
											size: "sm"
										}), Z(" " + U(i.processSummary.successfulCount), 1)])]),
										_: 1
									})]),
									_: 1
								})]),
								_: 1
							})]),
							Y("div", Va, [J(f, null, {
								default: X(() => [J(d, null, {
									default: X(() => [J(u, null, {
										default: X(() => [Y("div", Ha, [J(l, {
											color: "red",
											name: "error",
											size: "sm"
										}), Z(" " + U(i.processSummary.errorsCount), 1)])]),
										_: 1
									})]),
									_: 1
								})]),
								_: 1
							})]),
							Y("div", Ua, [J(f, null, {
								default: X(() => [J(d, null, {
									default: X(() => [J(u, null, {
										default: X(() => [Y("div", Wa, [J(l, {
											color: "grey",
											name: "timer_off",
											size: "sm"
										}), Z(" " + U(i.processSummary.misfiredCount), 1)])]),
										_: 1
									})]),
									_: 1
								})]),
								_: 1
							})])
						]),
						_: 1
					}),
					J(c, { inset: "" }),
					n.orchestraUiReadOnly ? K("", !0) : (W(), ja(s, { key: 0 }, {
						default: X(() => [J(p, {
							label: e.$vui.i18n().vuiOrchestra.orchestra.executeNow,
							onClick: t[0] ||= (e) => a.executeNow(i.processInfo.definitionId),
							color: "indigo",
							class: "q-mx-auto"
						}, null, 8, ["label"])]),
						_: 1
					}))
				]),
				_: 1
			}),
			i.processInfo.metadatas && Object.keys(i.processInfo.metadatas).length > 0 ? (W(), ja(m, {
				key: 0,
				class: "my-card q-mt-lg text-primary"
			}, {
				default: X(() => [
					J(s, null, {
						default: X(() => [Y("div", Ga, U(e.$vui.i18n().vuiOrchestra.orchestra.functionalId), 1)]),
						_: 1
					}),
					J(c, { inset: "" }),
					J(s, null, {
						default: X(() => [J(f, { dense: "" }, {
							default: X(() => [(W(!0), G(Na, null, Ma(i.processInfo.metadatas, (e, t) => (W(), G("div", { key: t }, [J(d, null, {
								default: X(() => [J(u, { class: "text-weight-medium" }, {
									default: X(() => [Z(U(t), 1)]),
									_: 2
								}, 1024), J(u, { side: "" }, {
									default: X(() => [Z(U(e), 1)]),
									_: 2
								}, 1024)]),
								_: 2
							}, 1024)]))), 128))]),
							_: 1
						})]),
						_: 1
					})
				]),
				_: 1
			})) : K("", !0),
			J(m, { class: "my-card q-mt-lg text-primary" }, {
				default: X(() => [
					J(s, null, {
						default: X(() => [Y("div", Ka, [Z(U(e.$vui.i18n().vuiOrchestra.orchestra.technicalId) + " ", 1), n.orchestraUiReadOnly ? K("", !0) : (W(), ja(p, {
							key: 0,
							round: "",
							color: "indigo",
							icon: "edit",
							class: "q-ml-sm",
							size: "sm",
							onClick: t[1] ||= (e) => i.editMode.technical = !i.editMode.technical
						}))])]),
						_: 1
					}),
					J(c, { inset: "" }),
					J(s, { class: "q-gutter-sm" }, {
						default: X(() => [Y("div", null, [J(f, { dense: "" }, {
							default: X(() => [i.editMode.technical ? (W(), G("div", qa, [J(_, { onSubmit: t[8] ||= (e) => a.onSubmit("technical") }, {
								default: X(() => [
									J(d, null, {
										default: X(() => [J(u, { class: "text-weight-medium" }, {
											default: X(() => [Z(U(e.$vui.i18n().vuiOrchestra.orchestra.processName), 1)]),
											_: 1
										}), J(u, { side: "" }, {
											default: X(() => [Z(U(i.processInfo.definitionId), 1)]),
											_: 1
										})]),
										_: 1
									}),
									J(d, null, {
										default: X(() => [J(u, { class: "text-weight-medium" }, {
											default: X(() => [Z(U(e.$vui.i18n().vuiOrchestra.orchestra.cronExpression), 1)]),
											_: 1
										}), J(u, { side: "" }, {
											default: X(() => [J(h, {
												modelValue: i.form.technical.cronExpression,
												"onUpdate:modelValue": t[2] ||= (e) => i.form.technical.cronExpression = e,
												dense: !0,
												placeholder: "* * * * * ? *"
											}, null, 8, ["modelValue"])]),
											_: 1
										})]),
										_: 1
									}),
									J(d, null, {
										default: X(() => [J(u, { class: "text-weight-medium" }, {
											default: X(() => [Z(U(e.$vui.i18n().vuiOrchestra.orchestra.active), 1)]),
											_: 1
										}), J(u, { side: "" }, {
											default: X(() => [Y("div", Ja, [J(g, {
												size: "xs",
												modelValue: i.form.technical.active,
												"onUpdate:modelValue": t[3] ||= (e) => i.form.technical.active = e,
												val: "true",
												label: e.$vui.i18n().vuiOrchestra.orchestra.yes
											}, null, 8, ["modelValue", "label"]), J(g, {
												size: "xs",
												modelValue: i.form.technical.active,
												"onUpdate:modelValue": t[4] ||= (e) => i.form.technical.active = e,
												val: "false",
												label: e.$vui.i18n().vuiOrchestra.orchestra.no
											}, null, 8, ["modelValue", "label"])])]),
											_: 1
										})]),
										_: 1
									}),
									J(d, null, {
										default: X(() => [J(u, { class: "text-weight-medium" }, {
											default: X(() => [Z(U(e.$vui.i18n().vuiOrchestra.orchestra.multiExecution), 1)]),
											_: 1
										}), J(u, { side: "" }, {
											default: X(() => [Y("div", Ya, [J(g, {
												size: "xs",
												modelValue: i.form.technical.multiExecution,
												"onUpdate:modelValue": t[5] ||= (e) => i.form.technical.multiExecution = e,
												val: "true",
												label: e.$vui.i18n().vuiOrchestra.orchestra.yes
											}, null, 8, ["modelValue", "label"]), J(g, {
												size: "xs",
												modelValue: i.form.technical.multiExecution,
												"onUpdate:modelValue": t[6] ||= (e) => i.form.technical.multiExecution = e,
												val: "false",
												label: e.$vui.i18n().vuiOrchestra.orchestra.no
											}, null, 8, ["modelValue", "label"])])]),
											_: 1
										})]),
										_: 1
									}),
									J(d, null, {
										default: X(() => [J(u, { class: "text-weight-medium" }, {
											default: X(() => [Z(U(e.$vui.i18n().vuiOrchestra.orchestra.rescuePeriod), 1)]),
											_: 1
										}), J(u, { side: "" }, {
											default: X(() => [J(h, {
												modelValue: i.form.technical.rescuePeriod,
												"onUpdate:modelValue": t[7] ||= (e) => i.form.technical.rescuePeriod = e,
												modelModifiers: { number: !0 },
												type: "number",
												dense: !0
											}, null, 8, ["modelValue"])]),
											_: 1
										})]),
										_: 1
									}),
									Y("div", null, [J(d, null, {
										default: X(() => [n.orchestraUiReadOnly ? K("", !0) : (W(), ja(p, {
											key: 0,
											label: e.$vui.i18n().vuiOrchestra.orchestra.submit,
											type: "submit",
											color: "indigo",
											class: "q-mx-auto"
										}, null, 8, ["label"]))]),
										_: 1
									})])
								]),
								_: 1
							})])) : (W(), G("div", Xa, [
								J(d, null, {
									default: X(() => [J(u, { class: "text-weight-medium" }, {
										default: X(() => [Z(U(e.$vui.i18n().vuiOrchestra.orchestra.processName), 1)]),
										_: 1
									}), J(u, { side: "" }, {
										default: X(() => [Z(U(i.processInfo.definitionId), 1)]),
										_: 1
									})]),
									_: 1
								}),
								J(d, null, {
									default: X(() => [J(u, { class: "text-weight-medium" }, {
										default: X(() => [Z(U(e.$vui.i18n().vuiOrchestra.orchestra.cronExpression), 1)]),
										_: 1
									}), J(u, { side: "" }, {
										default: X(() => [Z(U(i.processInfo.triggeringStrategy ? i.processInfo.triggeringStrategy.cronExpression : "-"), 1)]),
										_: 1
									})]),
									_: 1
								}),
								J(d, null, {
									default: X(() => [J(u, { class: "text-weight-medium" }, {
										default: X(() => [Z(U(e.$vui.i18n().vuiOrchestra.orchestra.active), 1)]),
										_: 1
									}), J(u, { side: "" }, {
										default: X(() => [Z(U(Object.entries(i.processInfo).length ? i.processInfo.active ? e.$vui.i18n().vuiOrchestra.orchestra.yes : e.$vui.i18n().vuiOrchestra.orchestra.no : "-"), 1)]),
										_: 1
									})]),
									_: 1
								}),
								J(d, null, {
									default: X(() => [J(u, { class: "text-weight-medium" }, {
										default: X(() => [Z(U(e.$vui.i18n().vuiOrchestra.orchestra.multiExecution), 1)]),
										_: 1
									}), J(u, { side: "" }, {
										default: X(() => [Z(U(i.processInfo.triggeringStrategy ? i.processInfo.triggeringStrategy.multiExecution ? e.$vui.i18n().vuiOrchestra.orchestra.yes : e.$vui.i18n().vuiOrchestra.orchestra.no : "-"), 1)]),
										_: 1
									})]),
									_: 1
								}),
								J(d, null, {
									default: X(() => [J(u, { class: "text-weight-medium" }, {
										default: X(() => [Z(U(e.$vui.i18n().vuiOrchestra.orchestra.rescuePeriod), 1)]),
										_: 1
									}), J(u, { side: "" }, {
										default: X(() => [Z(U(i.processInfo.triggeringStrategy ? i.processInfo.triggeringStrategy.rescuePeriodInSeconds : "-"), 1)]),
										_: 1
									})]),
									_: 1
								})
							]))]),
							_: 1
						})])]),
						_: 1
					})
				]),
				_: 1
			}),
			J(m, { class: "my-card q-mt-lg text-primary" }, {
				default: X(() => [
					J(s, null, {
						default: X(() => [Y("div", Za, [Z(U(e.$vui.i18n().vuiOrchestra.orchestra.settings) + " ", 1), n.orchestraUiReadOnly ? K("", !0) : (W(), ja(p, {
							key: 0,
							round: "",
							color: "indigo",
							icon: "edit",
							class: "q-ml-sm",
							size: "sm",
							onClick: t[9] ||= (e) => i.editMode.settings = !i.editMode.settings
						}))])]),
						_: 1
					}),
					J(c, { inset: "" }),
					J(s, null, {
						default: X(() => [J(f, { dense: "" }, {
							default: X(() => [i.processInfo.triggeringStrategy && i.processInfo.triggeringStrategy.initialParams ? (W(), G("div", Qa, [i.editMode.settings ? (W(), G("div", $a, [J(_, { onSubmit: t[10] ||= (e) => a.onSubmit("settings") }, {
								default: X(() => [(W(!0), G(Na, null, Ma(i.processInfo.triggeringStrategy.initialParams, (t, n) => (W(), G("div", { key: n }, [J(d, null, {
									default: X(() => [J(u, { class: "text-weight-medium" }, {
										default: X(() => [Z(U(n), 1)]),
										_: 2
									}, 1024), J(u, { side: "" }, {
										default: X(() => [J(h, {
											modelValue: i.form.settings[n],
											"onUpdate:modelValue": (e) => i.form.settings[n] = e,
											dense: !0,
											rules: [(t) => t && t.length > 0 || e.$vui.i18n().vuiOrchestra.orchestra.fieldCannotBeEmpty]
										}, null, 8, [
											"modelValue",
											"onUpdate:modelValue",
											"rules"
										])]),
										_: 2
									}, 1024)]),
									_: 2
								}, 1024)]))), 128)), Y("div", null, [J(d, null, {
									default: X(() => [n.orchestraUiReadOnly ? K("", !0) : (W(), ja(p, {
										key: 0,
										label: e.$vui.i18n().vuiOrchestra.orchestra.submit,
										type: "submit",
										color: "indigo",
										class: "q-mx-auto"
									}, null, 8, ["label"]))]),
									_: 1
								})])]),
								_: 1
							})])) : (W(), G("div", eo, [(W(!0), G(Na, null, Ma(i.processInfo.triggeringStrategy.initialParams, (e, t) => (W(), G("div", { key: t }, [J(d, null, {
								default: X(() => [J(u, { class: "text-weight-medium" }, {
									default: X(() => [Z(U(t), 1)]),
									_: 2
								}, 1024), J(u, { side: "" }, {
									default: X(() => [Z(U(e), 1)]),
									_: 2
								}, 1024)]),
								_: 2
							}, 1024)]))), 128))]))])) : K("", !0)]),
							_: 1
						})]),
						_: 1
					})
				]),
				_: 1
			})
		])]),
		after: X(() => [Y("div", to, [Y("div", no, [Y("div", ro, U(e.$vui.i18n().vuiOrchestra.orchestra.executions), 1), Y("div", io, [J(y, {
			modelValue: i.filterTab,
			"onUpdate:modelValue": t[14] ||= (e) => i.filterTab = e,
			"inline-label": "",
			class: "text-primary q-mx-auto"
		}, {
			default: X(() => [
				J(v, {
					name: "all",
					icon: "list",
					label: e.$vui.i18n().vuiOrchestra.orchestra.all,
					onClick: t[11] ||= (e) => a.updateExecutions("")
				}, null, 8, ["label"]),
				J(v, {
					name: "done",
					icon: "done",
					label: e.$vui.i18n().vuiOrchestra.orchestra.done,
					onClick: t[12] ||= (e) => a.updateExecutions("DONE"),
					class: "text-green"
				}, null, 8, ["label"]),
				J(v, {
					name: "error",
					icon: "error",
					label: e.$vui.i18n().vuiOrchestra.orchestra.error,
					onClick: t[13] ||= (e) => a.updateExecutions("ERROR"),
					class: "text-red"
				}, null, 8, ["label"])
			]),
			_: 1
		}, 8, ["modelValue"])])]), J(C, {
			onLoad: a.onLoad,
			offset: 50,
			"scroll-target": "div.q-splitter__panel.q-splitter__after.col"
		}, {
			loading: X(() => [Y("div", vo, [!i.errorMessage && !i.executionsLoaded ? (W(), G("div", yo, [J(o, {
				color: "primary",
				size: "2em"
			})])) : (W(), G("div", bo, U(i.errorMessage), 1))])]),
			default: X(() => [J(f, {
				bordered: "",
				class: "rounded-borders q-mt-sm"
			}, {
				default: X(() => [(W(!0), G(Na, null, Ma(i.executions, (r) => (W(), G("div", { key: r.preId }, [J(te, {
					"expand-separator": "",
					onShow: (e) => a.fetchActivities(r.preId)
				}, {
					header: X(() => [J(u, { avatar: "" }, {
						default: X(() => [J(b, {
							icon: a.getIconFromExecutionState(r.status),
							color: a.getColorFromExecutionState(r.status),
							"text-color": "white"
						}, null, 8, ["icon", "color"]), J(x, null, {
							default: X(() => [Z(U(r.status), 1)]),
							_: 2
						}, 1024)]),
						_: 2
					}, 1024), J(u, { class: "text-weight-medium" }, {
						default: X(() => [Z(U(a.formatDate(r.beginTime)) + " ", 1), J(x, {
							delay: 1e3,
							anchor: "top middle"
						}, {
							default: X(() => [Z(" preId: " + U(r.preId), 1)]),
							_: 2
						}, 1024)]),
						_: 2
					}, 1024)]),
					default: X(() => [J(m, null, {
						default: X(() => [J(c), J(s, null, {
							default: X(() => [J(ne, {
								modelValue: i.splitterModelExecutions,
								"onUpdate:modelValue": t[15] ||= (e) => i.splitterModelExecutions = e
							}, {
								before: X(() => [J(y, {
									modelValue: i.tabs[r.preId],
									"onUpdate:modelValue": (e) => i.tabs[r.preId] = e,
									vertical: "",
									class: "text-primary"
								}, {
									default: X(() => [
										J(v, {
											name: "info",
											label: e.$vui.i18n().vuiOrchestra.orchestra.informations
										}, null, 8, ["label"]),
										J(v, {
											name: "activities",
											label: e.$vui.i18n().vuiOrchestra.orchestra.activities
										}, null, 8, ["label"]),
										J(v, {
											name: "support",
											label: e.$vui.i18n().vuiOrchestra.orchestra.support
										}, null, 8, ["label"])
									]),
									_: 1
								}, 8, ["modelValue", "onUpdate:modelValue"])]),
								after: X(() => [J(S, {
									modelValue: i.tabs[r.preId],
									"onUpdate:modelValue": (e) => i.tabs[r.preId] = e,
									animated: "",
									vertical: "",
									"transition-prev": "jump-up",
									"transition-next": "jump-up"
								}, {
									default: X(() => [
										J(ee, { name: "info" }, {
											default: X(() => [
												Y("div", ao, U(e.$vui.i18n().vuiOrchestra.orchestra.informations), 1),
												J(c, { class: "q-mt-sm q-mb-md" }),
												Y("div", oo, [J(f, { dense: "" }, {
													default: X(() => [
														J(d, null, {
															default: X(() => [J(u, null, {
																default: X(() => [Y("div", so, U(e.$vui.i18n().vuiOrchestra.orchestra.startTime), 1)]),
																_: 1
															}), J(u, {
																side: "",
																class: "text-primary"
															}, {
																default: X(() => [Z(U(a.formatDate(r.beginTime)), 1)]),
																_: 2
															}, 1024)]),
															_: 2
														}, 1024),
														J(d, null, {
															default: X(() => [J(u, null, {
																default: X(() => [Y("div", co, U(e.$vui.i18n().vuiOrchestra.orchestra.endTime), 1)]),
																_: 1
															}), J(u, {
																side: "",
																class: "text-primary"
															}, {
																default: X(() => [Z(U(a.formatDate(r.endTime)), 1)]),
																_: 2
															}, 1024)]),
															_: 2
														}, 1024),
														J(d, null, {
															default: X(() => [J(u, null, {
																default: X(() => [Y("div", lo, U(e.$vui.i18n().vuiOrchestra.orchestra.duration), 1)]),
																_: 1
															}), J(u, {
																side: "",
																class: "text-primary"
															}, {
																default: X(() => [Z(U(r.executionTime) + "s ", 1)]),
																_: 2
															}, 1024)]),
															_: 2
														}, 1024)
													]),
													_: 2
												}, 1024)])
											]),
											_: 2
										}, 1024),
										J(ee, { name: "activities" }, {
											default: X(() => [
												Y("div", uo, U(e.$vui.i18n().vuiOrchestra.orchestra.activities), 1),
												J(c, { class: "q-mt-sm q-mb-md" }),
												J(f, {
													bordered: "",
													class: "rounded-borders"
												}, {
													default: X(() => [(W(!0), G(Na, null, Ma(i.activities[r.preId], (t) => (W(), G("div", { key: t.aceId }, [J(te, { "expand-separator": "" }, {
														header: X(() => [J(u, { avatar: "" }, {
															default: X(() => [J(b, {
																icon: a.getIconFromExecutionState(t.status),
																color: a.getColorFromExecutionState(t.status),
																"text-color": "white"
															}, null, 8, ["icon", "color"]), J(x, null, {
																default: X(() => [Z(U(t.status), 1)]),
																_: 2
															}, 1024)]),
															_: 2
														}, 1024), J(u, { class: "text-weight-medium" }, {
															default: X(() => [Z(U(t.label) + " ", 1), J(x, {
																delay: 1e3,
																anchor: "top middle"
															}, {
																default: X(() => [Z(" aceId: " + U(t.aceId), 1)]),
																_: 2
															}, 1024)]),
															_: 2
														}, 1024)]),
														default: X(() => [J(m, null, {
															default: X(() => [J(c), J(s, null, {
																default: X(() => [Y("div", fo, [J(f, { dense: "" }, {
																	default: X(() => [
																		J(d, null, {
																			default: X(() => [J(u, null, {
																				default: X(() => [Y("div", po, U(e.$vui.i18n().vuiOrchestra.orchestra.startTime), 1)]),
																				_: 1
																			}), J(u, {
																				side: "",
																				class: "text-primary"
																			}, {
																				default: X(() => [Z(U(a.formatDate(t.beginTime)), 1)]),
																				_: 2
																			}, 1024)]),
																			_: 2
																		}, 1024),
																		J(d, null, {
																			default: X(() => [J(u, null, {
																				default: X(() => [Y("div", mo, U(e.$vui.i18n().vuiOrchestra.orchestra.endTime), 1)]),
																				_: 1
																			}), J(u, {
																				side: "",
																				class: "text-primary"
																			}, {
																				default: X(() => [Z(U(a.formatDate(t.endTime)), 1)]),
																				_: 2
																			}, 1024)]),
																			_: 2
																		}, 1024),
																		J(d, null, {
																			default: X(() => [J(u, null, {
																				default: X(() => [Y("div", ho, U(e.$vui.i18n().vuiOrchestra.orchestra.duration), 1)]),
																				_: 1
																			}), J(u, {
																				side: "",
																				class: "text-primary"
																			}, {
																				default: X(() => [Z(U(t.executionTime) + "s ", 1)]),
																				_: 2
																			}, 1024)]),
																			_: 2
																		}, 1024),
																		t.hasLogFile ? (W(), ja(d, { key: 0 }, {
																			default: X(() => [J(u, null, {
																				default: X(() => [Y("div", go, U(e.$vui.i18n().vuiOrchestra.orchestra.logFile), 1)]),
																				_: 1
																			}), J(u, {
																				side: "",
																				class: "text-primary"
																			}, {
																				default: X(() => [J(p, {
																					type: "a",
																					icon: "description",
																					flat: "",
																					padding: "none",
																					title: e.$vui.i18n().vuiOrchestra.orchestra.logFile,
																					href: n.apiUrl + "/executions/" + r.preId + "/activities/" + t.aceId + "/attachment"
																				}, null, 8, ["title", "href"])]),
																				_: 2
																			}, 1024)]),
																			_: 2
																		}, 1024)) : K("", !0),
																		t.hasTechnicalLog ? (W(), ja(d, { key: 1 }, {
																			default: X(() => [J(u, null, {
																				default: X(() => [Y("div", _o, U(e.$vui.i18n().vuiOrchestra.orchestra.technicalLogFile), 1)]),
																				_: 1
																			}), J(u, {
																				side: "",
																				class: "text-primary"
																			}, {
																				default: X(() => [J(p, {
																					type: "a",
																					icon: "description",
																					flat: "",
																					padding: "none",
																					title: e.$vui.i18n().vuiOrchestra.orchestra.technicalLogFile,
																					href: n.apiUrl + "/executions/" + r.preId + "/activities/" + t.aceId + "/logFile"
																				}, null, 8, ["title", "href"])]),
																				_: 2
																			}, 1024)]),
																			_: 2
																		}, 1024)) : K("", !0)
																	]),
																	_: 2
																}, 1024)])]),
																_: 2
															}, 1024)]),
															_: 2
														}, 1024)]),
														_: 2
													}, 1024), J(c)]))), 128))]),
													_: 2
												}, 1024)
											]),
											_: 2
										}, 1024),
										K("", !0)
									]),
									_: 2
								}, 1032, ["modelValue", "onUpdate:modelValue"])]),
								_: 2
							}, 1032, ["modelValue"])]),
							_: 2
						}, 1024)]),
						_: 2
					}, 1024)]),
					_: 2
				}, 1032, ["onShow"]), J(c)]))), 128))]),
				_: 1
			})]),
			_: 1
		}, 8, ["onLoad"])])]),
		_: 1
	}, 8, ["modelValue"]);
}
var So = /*#__PURE__*/ ta(Aa, [["render", xo]]), Co = { data() {
	return {};
} }, wo = window.Vue.createElementVNode, To = window.Vue.openBlock, Eo = window.Vue.createElementBlock, Do = { class: "text-center" };
function Oo(e, t, n, r, i, a) {
	return To(), Eo("div", Do, [...t[0] ||= [
		wo("h2", null, "404 Not found", -1),
		wo("img", { src: "data:image/png;base64,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" }, null, -1),
		wo("h4", null, "Sorry, we couldn't find the url you're looking for!", -1)
	]]);
}
var ko = /*#__PURE__*/ ta(Co, [["render", Oo]]);
//#endregion
//#region src/router/index.js
window.Vue;
var Ao = Ft({
	history: Be(),
	routes: [
		{
			path: "/",
			name: "Home",
			component: ka
		},
		{
			path: "/process/:name",
			name: "Process",
			component: So,
			props: (e) => ({ processName: e.params.name })
		},
		{
			path: "/:pathMatch(.*)*",
			name: "NotFound",
			component: ko
		}
	]
}), jo = {
	name: "app",
	router: Ao,
	props: ["apiUrl", "readOnly"],
	data() {
		return { lang: "fr" };
	},
	methods: { changeLang: function() {
		window.Quasar.lang.set(this.lang == "fr" ? window.Quasar.lang.fr : window.Quasar.lang.en);
	} }
}, Mo = window.Vue.createElementVNode, Q = window.Vue.resolveComponent, No = window.Vue.withCtx, $ = window.Vue.createVNode, Po = window.Vue.createTextVNode, Fo = window.Vue.openBlock, Io = window.Vue.createBlock;
function Lo(e, t, n, r, i, a) {
	let o = Q("q-avatar"), s = Q("q-toolbar-title"), c = Q("q-space"), l = Q("q-btn-toggle"), u = Q("q-btn"), d = Q("q-toolbar"), f = Q("q-header"), p = Q("router-view"), m = Q("q-page-container"), h = Q("q-footer"), g = Q("q-layout");
	return Fo(), Io(g, { view: "hHh LpR fFf" }, {
		default: No(() => [
			$(f, {
				elevated: "",
				class: "text-white gradient-bg"
			}, {
				default: No(() => [$(d, { class: "" }, {
					default: No(() => [
						$(s, null, {
							default: No(() => [Mo("div", null, [$(o, { onClick: t[0] ||= (t) => e.$router.push("/") }, {
								default: No(() => [...t[3] ||= [Mo("img", { src: "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEYAAABGCAYAAABxLuKEAAALlklEQVR4nOxc6W9c5dU/d7+zefaxPXa8O4kJcTDgkDcvLwECL4bAGxISB6K3pAWpVdU/gA8JEFBaAqWhUYC2iCKVIlREi1pRqaWiRSzqAqGLQhYq8MRL7MT27DN3mbtVz9iT2LPeO3Nn7A/8pCi+9z5zzrlnzvY8z3mG1DQNvkIh8JUWYLXiK8WUALn4f9tcMvPjuVTGNMJdXst7Vpo4ZhrBIlA1eDidke81hZa6EFKcFuoRADiD5WLM2Dz322+9eWaHWRHnO/+95tKuweY2AFBMIlmAcDpz+sswf5XRzwmSosZ4GWK8lP0X52U8Icjwza1rPu73226AJRYDPT7roZt73Xe+92XUFPd6/4tI867B5m0A8Ccz6BXBhggnlVWKomqQFGQ1JmRfflERMoiyWvCOfT4r9PttB3PX5JJn/zwwHPzlB2PRUcUEszl9KQ1zqcz9fjtdF8UoKuyNCfLla15S1PhyK4CEKOOapi+O3jngew8A3s1dY3npuu/4++dPv31mnjZD+G9vbY/et6mlFQBEM+gtxfkwd+735+b7kQvEBRkXZbVqWhtb7eqBze1bAOCT3L18bX7x4HDbSyxpTrKK87IbAEZMIbYc105EU+tCYQ6fTWVqUgqOAdx1lf/NpUqBYunabaW+u2tjgKuaU46OhQTAMGTWo7XSykdGVkfn0iJ4bWTNTn/9GqfstzOP598vZhoXR69ped5OEzUx7PVas0KHwvw9AGCpiVgeLsS5UZRd/fbaFENgALev870KAJ/nPyvqMw6WfHrvpuZ4LQy7PBYM/R2K8A4U26qlVQTDkzGuG/1hZwjcQmFVK+f6DqfotlJPFntWKphEdg82H3MyZFUMg05GoxfjFEqPUU7aWxWhIhBlZd9s8kosD1RpNSi23Nrv/TkAjBd9XuqDFpp4bvdgIFwN0+5Fa8nhfIS/GwCs1dDKA3Yhxu9ZqgmvjcSxKgih2OK10U+Xel4u/SR3bgwcNxpr0PjWJnbZvbEwbzfJnbZMxbjOpTcoAgenBTeUlpAqF2PLF6XGlM3LdoY8YTTWdHlYIPK+w7iQdad9RugUgyApe2dThSWRz2bM5bd0ukrGlhwqFSyx3YPNP3Cx+hl3eSxFfT4U5u6q0Z2wC3FubzHiLguJ6/UnmsBg+1rvS6ViSw4VKzkLTfzwgWtb5vQw9dkotYmliooYivA2ALhbD50S2DoZ49uLPUAW6rUSutzpxh532sGS36s0Tk+Jm7xnQ+Bpv42qOLDXay35vcUFBSLpTNXFniApo/NF3CgHv72yfBYSh5v7vCdQrVZprK7anybxF/7/utYL5caggNbpZssadCjCowBs08MzD/hUjNtTLi/bGRxnyfL+tK3PE7PSxDO6GOoUTBgZCDzW6WJLDuj2WIAkypMbC/MoxuzSyXMptk1EuWC5ARiGQcBBlnSnJpaA/+n1HAWAqB6GumeLBA6vPjgcPFfqea/XUtHHk6ICc6mM4WKPy8j7wlzl1UVfmZpme79vmiHx5/XyNDKNlrf1eQ6u9xcmFq+VBI+N1kVrLMyh2bbHAF9qMsrt0TWQwMFdJAj7rBRs6XKj9JzWy9To+sJbD93QdjL/Zq9PfxY+H+ZpANhpgOdtkzHOq3ew315YWvzvet+XBA4/NcDT+C7BtWuchzYF7Zev0ZSo061/8pyWVLiUFHVnp5Qo7Y3ykm76TpbAUa2SQ4uDhmvWOA8ji9dNpMrtk3ce2tz2Qe6iw2XJmrARhML8duSBOobSUzHeULBGQdhnuzKNGRnwf4YDvG5IwGr3lTa0Og5u6XBm/+4z4EY5nI/wqOjQs+1x+2SMcxmlH1isaTpcLFzd6ngUAAwv8VW7hvnRNzYH30ZTBa+Owi8fnKTCxYR4X6VxSUG6L2bAjXJgKBwcDK6ODPj+CgC/Nkyglp3IXr/tia8PBzVkutUgFMm6k7vMEHoqxlW9mTbU5sDXBuwHq/18Laven25f532jw8VoRBW6OR/JZqfdZYagbFROcUUhqxqMzQvqYND5bi17WjVtB1go4vEen1Xp91vBweibxOXASyrMJMSSxV5CkPbFBUOJBOK8rJ2a5sFnZ/Eur7VqawETNvX/3eZkfoayUofHgrc7GdXIcloozN+K6q8ij5ipGPd/eukoqgahsKiemxWxjKLByHr/bwDgY/2SFKLmDSSGJJ702ykR6cNlpXCUpWw0oWsddjzKU2rxudMdk1F92SglKtpnM7w2m5Kz77Ip6FDbXOyjRt8jH2bsrE0EnexPcjGYJnHo8rBYi4NWKxkPcqeLcaHAnRJ8ZjQhlncjVdNgMppRT18UMEHWsqyQDHes9/0CAE7V8kJgVn8MTeBHWhz05SXQbJFlp/E+f9Z6ysaesTB/CypQl9yyTkTLu1FSUNTPZniYTkjL5L+hwykGHEzN1gImNg7NtTqY4/kFMLNgPXhLE10y9oxHeVJRl2WnuyZj2b2oAqBYgqzk7CUB56Xl3krhGNy21ofmQ2O1v46JHVUkgX8/2MTM5t9fKNFLW48oazAd5/fnriNpcX8qU+hGSWEhliArKRbAbuxxp1xW6glz3sbcVrNUs509WmpFhCYWrKe1qTBzhSL8fwFAEADsUzF+2TaLqmowERXVM5euxJJ8MCQGt/R7jwNAwRdTLUztwcNx+FGrg54u9RxZj9e2kLms1JW9oPGogCsqoLgyMhnjLi8Tooxz6iKvzSTksnJu7XLHrDTxrImvYnpzohBwsM/SFUphlLm6vRY8l7kkRYOZhLAzyok7OUlZnnGk4laSA0VgcFOv5wUAiJn5IvmNQ2bAdikpnhqPCt16Bouyqk3FBOh0s5kmFsucnIzZx+ZF4CooJIdb+jypHRsCiNd8zZIvQT3aWdN+O/MspbMEZkgc6/FaMEFWmZMTCcfpGQHTqxSWxFFsOWG2UqBefb44Bq8EnUzJWJMPFHsuJkT4cCwORux3W6/H9NiSQ70aoAW/gznCGJh2/208rs2n9a+92GgCbup1H0MZvkoZy6JuneE4wMtBJxvSMzbBS3B2No2lJRV4SV9b8K39njmGIp6rVc5SqGfLvOS304f1NDr+ZTyea8yGuZRUcfnCyZKwtdt9FGV0UyQtgnqfJXit3cWcKTdAUlT4ZCJ+WY4oL+PoXjncttY7RRH4iybKWYB6K0b1WOnHrFRpNv+YSqjcEiPRsq3wpWON10rB5k73ERTHTJd2CRpx+uStdhf7abEHqIb6c6iwLpvnpGyRVwyLm2evmC/mcjRCMZrLQj3iYApb1j6fTauzaalABlkFiHJygWaCTQwMrXEeRB5YR3mzaNR5pT92uNh38m9+OFa68WA2lYH8qnzHVf6TOMAb9RFxORp2kMvGkIey3eKLmI4LaigilOQvKhqWFK9E4R6vBdY1V78dYhSNPOF2st3JvpW7KBZb8jGXzlyuEO8c8L8PAH+oo3zL0NCjfxaaeNRrpZSkIMO/ppMVeSdFFRMkBdYHbNDttR5qjJQLaPSZyDNtTub1TybioPdMFJomjAz4fwcAH9VbuKVo+GFRliIO22hC9+HLDS0Otd3FNtRaYIVO0Y7tv671FT0TTBwDODAc/BUA/L0hki3l3WiGkO3/p4/svDrAVxp3e79X6fBYCs4SNQIrde76wv1DLS+WmyqQOAZfGw6+BgBnGyrZIlbsQHqThTq6Z7A5Wer5jgFfpqWJMW07xChW8qT+/J5rmp9rKjJVQPFn/3WtLwOArvWcemBFf8LASpPH7h9qKTgTde/VAQ7FoZWRagEr/dsO8Z0bA894rVfa1VDc2TfU8gIAzKykYCutGGBI4sSB4eBE7vqBodZIk4V6amWlWgWKAQB+ZL3/cLuTAY+FhN2bmp/S2+9fT9Rjw60aEO+cnT8bF2TH6FBLD1LWSgu0WhQDsqI9rGiamyHxuuwTGcWqUQwAUIs/wrHi1gKrTDGrCqsh+K5K/CcAAP//FP712Xu+8jIAAAAASUVORK5CYII=" }, null, -1)]]),
								_: 1
							}), t[4] ||= Po("Orchestra UI ", -1)])]),
							_: 1
						}),
						$(c),
						$(l, {
							modelValue: i.lang,
							"onUpdate:modelValue": t[1] ||= (e) => i.lang = e,
							options: [{
								label: "Fr",
								value: "fr"
							}, {
								label: "En",
								value: "en"
							}],
							onInput: t[2] ||= (e) => a.changeLang()
						}, null, 8, ["modelValue"]),
						$(u, {
							flat: "",
							round: "",
							icon: "power_settings_new"
						})
					]),
					_: 1
				})]),
				_: 1
			}),
			$(m, null, {
				default: No(() => [$(p, {
					"api-url": n.apiUrl,
					"orchestra-ui-read-only": n.readOnly
				}, null, 8, ["api-url", "orchestra-ui-read-only"])]),
				_: 1
			}),
			$(h, {
				elevated: "",
				class: "bg-grey-8 text-white"
			}, {
				default: No(() => [$(d, null, {
					default: No(() => [$(s, { class: "absolute-center" }, {
						default: No(() => [...t[5] ||= [Po(" Copyright © 2020 - 2026 ", -1)]]),
						_: 1
					})]),
					_: 1
				})]),
				_: 1
			})
		]),
		_: 1
	});
}
var Ro = /*#__PURE__*/ ta(jo, [["render", Lo]]), zo = {
	name: "app",
	router: Ao,
	data() {
		return { left: !1 };
	},
	props: ["apiUrl", "readOnly"]
}, Bo = window.Vue.resolveComponent, Vo = window.Vue.createVNode, Ho = window.Vue.openBlock, Uo = window.Vue.createElementBlock;
function Wo(e, t, n, r, i, a) {
	let o = Bo("router-view");
	return Ho(), Uo("div", null, [Vo(o, {
		"api-url": n.apiUrl,
		"orchestra-ui-read-only": n.readOnly
	}, null, 8, ["api-url", "orchestra-ui-read-only"])]);
}
var Go = /*#__PURE__*/ ta(zo, [["render", Wo]]), Ko = { orchestra: {
	title: {
		part1: "Exécutions du",
		part2: "au"
	},
	all: "tout",
	success: "succès",
	error: "erreur",
	misfired: "raté/échoué",
	previousWeek: "Semaine précédente",
	nextWeek: "Semaine suivante",
	processLabel: "Process",
	processName: "Code",
	state: "État",
	lastExecutionTime: "Dernière exécution",
	nextExecutionTime: "Prochaine exécution",
	connectionFailed: "La connection à l'API n'a pas pu être établie!",
	loading: "Chargement",
	noData: "Aucune donnée disponible",
	totalExecutions: "Exécutions totales de la semaine",
	functionalId: "Identification fonctionnelle",
	technicalId: "Identification technique ",
	cronExpression: "Expression cron",
	active: "Actif",
	multiExecution: "Autorise la multi-exécution",
	rescuePeriod: "Temps de validité d'une planification (s)",
	settings: "Paramètres",
	executions: "Exécutions",
	executeNow: "Programmer une éxécution",
	executeNowOk: "L'éxécution est programmée et se lancera dès que possible",
	executeNowErr: "Une erreur est survenue",
	done: "Terminé",
	informations: "Informations",
	activities: "Activités",
	logFile: "Fichier de log",
	technicalLogFile: "Log technique",
	support: "Prise en charge",
	startTime: "Date de début",
	endTime: "date de fin",
	duration: "Durée",
	supportDate: "Date de prise en charge",
	comment: "Commentaire",
	running: "Exécution en cours ...",
	yes: "Oui",
	no: "Non",
	noProcess: "Aucun process nommé",
	submit: "Soumettre",
	fieldCannotBeEmpty: "Ce champ ne peut pas être vide"
} }, qo = { orchestra: {
	title: {
		part1: "Executions from",
		part2: "to"
	},
	all: "all",
	success: "success",
	error: "error",
	misfired: "misfired",
	previousWeek: "Previous week",
	nextWeek: "Next week",
	processLabel: "Process",
	processName: "Code",
	state: "State",
	lastExecutionTime: "Last execution",
	nextExecutionTime: "Next execution",
	connectionFailed: "Connection to the API couldn't be established !",
	loading: "Loading",
	noData: "No data available",
	totalExecutions: "Total executions of the week",
	functionalId: "Functional identification",
	technicalId: "Technical identification",
	cronExpression: "Cron expression",
	active: "Active",
	multiExecution: "Allows multi-execution",
	rescuePeriod: "Validity of a planning (s)",
	settings: "Settings",
	executions: "Executions",
	executeNow: "Schedule execution",
	executeNowOk: "Execution is schedule, it will run as soon as possible",
	executeNowErr: "An error occured",
	done: "Done",
	informations: "Informations",
	activities: "Activities",
	logFile: "Log file",
	technicalLogFile: "Technical log",
	support: "Support",
	startTime: "Start time",
	endTime: "End time",
	duration: "Duration",
	supportDate: "Time of support",
	comment: "Comment",
	running: "Running ...",
	yes: "Yes",
	no: "No",
	noProcess: "No process named",
	submit: "Submit",
	fieldCannotBeEmpty: "This field can't be empty"
} }, Jo = { install: function(e, t) {
	e.use(Ao), e.component("vui-orchestra-standalone", Ro), e.component("vui-orchestra", Go), e.component("vui-orchestra-home", ka), e.component("vui-orchestra-process", So), VertigoUi.lang.enUS.vuiOrchestra = qo, VertigoUi.lang.fr.vuiOrchestra = Ko;
} };
window.VertigoOrchestraUi = Jo;
//#endregion
export { Jo as default };

//# sourceMappingURL=vertigo-orchestra-ui.es.js.map