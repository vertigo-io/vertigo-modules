/*!
  * vue-router v4.4.2
  * (c) 2024 Eduardo San Martin Morote
  * @license MIT
  */
window.Vue.getCurrentInstance;
const Le = window.Vue.inject;
window.Vue.onUnmounted;
window.Vue.onDeactivated;
window.Vue.onActivated;
const ie = window.Vue.computed, Pe = window.Vue.unref;
window.Vue.watchEffect;
const _n = window.Vue.defineComponent, lr = window.Vue.reactive, bn = window.Vue.h, ut = window.Vue.provide, cr = window.Vue.ref, ur = window.Vue.watch, dr = window.Vue.shallowRef, fr = window.Vue.shallowReactive, hr = window.Vue.nextTick, ve = typeof document < "u";
function pr(e) {
  return e.__esModule || e[Symbol.toStringTag] === "Module";
}
const L = Object.assign;
function dt(e, t) {
  const n = {};
  for (const r in t) {
    const s = t[r];
    n[r] = ne(s) ? s.map(e) : e(s);
  }
  return n;
}
const Ne = () => {
}, ne = Array.isArray, vn = /#/g, mr = /&/g, gr = /\//g, wr = /=/g, yr = /\?/g, En = /\+/g, _r = /%5B/g, br = /%5D/g, Rn = /%5E/g, vr = /%60/g, xn = /%7B/g, Er = /%7C/g, Sn = /%7D/g, Rr = /%20/g;
function Tt(e) {
  return encodeURI("" + e).replace(Er, "|").replace(_r, "[").replace(br, "]");
}
function xr(e) {
  return Tt(e).replace(xn, "{").replace(Sn, "}").replace(Rn, "^");
}
function _t(e) {
  return Tt(e).replace(En, "%2B").replace(Rr, "+").replace(vn, "%23").replace(mr, "%26").replace(vr, "`").replace(xn, "{").replace(Sn, "}").replace(Rn, "^");
}
function Sr(e) {
  return _t(e).replace(wr, "%3D");
}
function Or(e) {
  return Tt(e).replace(vn, "%23").replace(yr, "%3F");
}
function qr(e) {
  return e == null ? "" : Or(e).replace(gr, "%2F");
}
function Fe(e) {
  try {
    return decodeURIComponent("" + e);
  } catch {
  }
  return "" + e;
}
const Ar = /\/$/, Tr = (e) => e.replace(Ar, "");
function ft(e, t, n = "/") {
  let r, s = {}, o = "", i = "";
  const l = t.indexOf("#");
  let c = t.indexOf("?");
  return l < c && l >= 0 && (c = -1), c > -1 && (r = t.slice(0, c), o = t.slice(c + 1, l > -1 ? l : t.length), s = e(o)), l > -1 && (r = r || t.slice(0, l), i = t.slice(l, t.length)), r = Nr(r ?? t, n), {
    fullPath: r + (o && "?") + o + i,
    path: r,
    query: s,
    hash: Fe(i)
  };
}
function kr(e, t) {
  const n = t.query ? e(t.query) : "";
  return t.path + (n && "?") + n + (t.hash || "");
}
function Mt(e, t) {
  return !t || !e.toLowerCase().startsWith(t.toLowerCase()) ? e : e.slice(t.length) || "/";
}
function Cr(e, t, n) {
  const r = t.matched.length - 1, s = n.matched.length - 1;
  return r > -1 && r === s && Re(t.matched[r], n.matched[s]) && On(t.params, n.params) && e(t.query) === e(n.query) && t.hash === n.hash;
}
function Re(e, t) {
  return (e.aliasOf || e) === (t.aliasOf || t);
}
function On(e, t) {
  if (Object.keys(e).length !== Object.keys(t).length)
    return !1;
  for (const n in e)
    if (!Pr(e[n], t[n]))
      return !1;
  return !0;
}
function Pr(e, t) {
  return ne(e) ? $t(e, t) : ne(t) ? $t(t, e) : e === t;
}
function $t(e, t) {
  return ne(t) ? e.length === t.length && e.every((n, r) => n === t[r]) : e.length === 1 && e[0] === t;
}
function Nr(e, t) {
  if (e.startsWith("/"))
    return e;
  if (!e)
    return t;
  const n = t.split("/"), r = e.split("/"), s = r[r.length - 1];
  (s === ".." || s === ".") && r.push("");
  let o = n.length - 1, i, l;
  for (i = 0; i < r.length; i++)
    if (l = r[i], l !== ".")
      if (l === "..")
        o > 1 && o--;
      else
        break;
  return n.slice(0, o).join("/") + "/" + r.slice(i).join("/");
}
const ue = {
  path: "/",
  // TODO: could we use a symbol in the future?
  name: void 0,
  params: {},
  query: {},
  hash: "",
  fullPath: "/",
  matched: [],
  meta: {},
  redirectedFrom: void 0
};
var Ue;
(function(e) {
  e.pop = "pop", e.push = "push";
})(Ue || (Ue = {}));
var Ve;
(function(e) {
  e.back = "back", e.forward = "forward", e.unknown = "";
})(Ve || (Ve = {}));
function Vr(e) {
  if (!e)
    if (ve) {
      const t = document.querySelector("base");
      e = t && t.getAttribute("href") || "/", e = e.replace(/^\w+:\/\/[^\/]+/, "");
    } else
      e = "/";
  return e[0] !== "/" && e[0] !== "#" && (e = "/" + e), Tr(e);
}
const Lr = /^[^#]+#/;
function Fr(e, t) {
  return e.replace(Lr, "#") + t;
}
function Ur(e, t) {
  const n = document.documentElement.getBoundingClientRect(), r = e.getBoundingClientRect();
  return {
    behavior: t.behavior,
    left: r.left - n.left - (t.left || 0),
    top: r.top - n.top - (t.top || 0)
  };
}
const Xe = () => ({
  left: window.scrollX,
  top: window.scrollY
});
function Br(e) {
  let t;
  if ("el" in e) {
    const n = e.el, r = typeof n == "string" && n.startsWith("#"), s = typeof n == "string" ? r ? document.getElementById(n.slice(1)) : document.querySelector(n) : n;
    if (!s)
      return;
    t = Ur(s, e);
  } else
    t = e;
  "scrollBehavior" in document.documentElement.style ? window.scrollTo(t) : window.scrollTo(t.left != null ? t.left : window.scrollX, t.top != null ? t.top : window.scrollY);
}
function jt(e, t) {
  return (history.state ? history.state.position - t : -1) + e;
}
const bt = /* @__PURE__ */ new Map();
function Dr(e, t) {
  bt.set(e, t);
}
function Ir(e) {
  const t = bt.get(e);
  return bt.delete(e), t;
}
let Mr = () => location.protocol + "//" + location.host;
function qn(e, t) {
  const { pathname: n, search: r, hash: s } = t, o = e.indexOf("#");
  if (o > -1) {
    let l = s.includes(e.slice(o)) ? e.slice(o).length : 1, c = s.slice(l);
    return c[0] !== "/" && (c = "/" + c), Mt(c, "");
  }
  return Mt(n, e) + r + s;
}
function $r(e, t, n, r) {
  let s = [], o = [], i = null;
  const l = ({ state: g }) => {
    const m = qn(e, location), w = n.value, _ = t.value;
    let q = 0;
    if (g) {
      if (n.value = m, t.value = g, i && i === w) {
        i = null;
        return;
      }
      q = _ ? g.position - _.position : 0;
    } else
      r(m);
    s.forEach((A) => {
      A(n.value, w, {
        delta: q,
        type: Ue.pop,
        direction: q ? q > 0 ? Ve.forward : Ve.back : Ve.unknown
      });
    });
  };
  function c() {
    i = n.value;
  }
  function d(g) {
    s.push(g);
    const m = () => {
      const w = s.indexOf(g);
      w > -1 && s.splice(w, 1);
    };
    return o.push(m), m;
  }
  function a() {
    const { history: g } = window;
    g.state && g.replaceState(L({}, g.state, { scroll: Xe() }), "");
  }
  function u() {
    for (const g of o)
      g();
    o = [], window.removeEventListener("popstate", l), window.removeEventListener("beforeunload", a);
  }
  return window.addEventListener("popstate", l), window.addEventListener("beforeunload", a, {
    passive: !0
  }), {
    pauseListeners: c,
    listen: d,
    destroy: u
  };
}
function Wt(e, t, n, r = !1, s = !1) {
  return {
    back: e,
    current: t,
    forward: n,
    replaced: r,
    position: window.history.length,
    scroll: s ? Xe() : null
  };
}
function jr(e) {
  const { history: t, location: n } = window, r = {
    value: qn(e, n)
  }, s = { value: t.state };
  s.value || o(r.value, {
    back: null,
    current: r.value,
    forward: null,
    // the length is off by one, we need to decrease it
    position: t.length - 1,
    replaced: !0,
    // don't add a scroll as the user may have an anchor, and we want
    // scrollBehavior to be triggered without a saved position
    scroll: null
  }, !0);
  function o(c, d, a) {
    const u = e.indexOf("#"), g = u > -1 ? (n.host && document.querySelector("base") ? e : e.slice(u)) + c : Mr() + e + c;
    try {
      t[a ? "replaceState" : "pushState"](d, "", g), s.value = d;
    } catch (m) {
      console.error(m), n[a ? "replace" : "assign"](g);
    }
  }
  function i(c, d) {
    const a = L({}, t.state, Wt(
      s.value.back,
      // keep back and forward entries but override current position
      c,
      s.value.forward,
      !0
    ), d, { position: s.value.position });
    o(c, a, !0), r.value = c;
  }
  function l(c, d) {
    const a = L(
      {},
      // use current history state to gracefully handle a wrong call to
      // history.replaceState
      // https://github.com/vuejs/router/issues/366
      s.value,
      t.state,
      {
        forward: c,
        scroll: Xe()
      }
    );
    o(a.current, a, !0);
    const u = L({}, Wt(r.value, c, null), { position: a.position + 1 }, d);
    o(c, u, !1), r.value = c;
  }
  return {
    location: r,
    state: s,
    push: l,
    replace: i
  };
}
function Wr(e) {
  e = Vr(e);
  const t = jr(e), n = $r(e, t.state, t.location, t.replace);
  function r(o, i = !0) {
    i || n.pauseListeners(), history.go(o);
  }
  const s = L({
    // it's overridden right after
    location: "",
    base: e,
    go: r,
    createHref: Fr.bind(null, e)
  }, t, n);
  return Object.defineProperty(s, "location", {
    enumerable: !0,
    get: () => t.location.value
  }), Object.defineProperty(s, "state", {
    enumerable: !0,
    get: () => t.state.value
  }), s;
}
function Hr(e) {
  return e = location.host ? e || location.pathname + location.search : "", e.includes("#") || (e += "#"), Wr(e);
}
function zr(e) {
  return typeof e == "string" || e && typeof e == "object";
}
function An(e) {
  return typeof e == "string" || typeof e == "symbol";
}
const Tn = Symbol("");
var Ht;
(function(e) {
  e[e.aborted = 4] = "aborted", e[e.cancelled = 8] = "cancelled", e[e.duplicated = 16] = "duplicated";
})(Ht || (Ht = {}));
function xe(e, t) {
  return L(new Error(), {
    type: e,
    [Tn]: !0
  }, t);
}
function ae(e, t) {
  return e instanceof Error && Tn in e && (t == null || !!(e.type & t));
}
const zt = "[^/]+?", Kr = {
  sensitive: !1,
  strict: !1,
  start: !0,
  end: !0
}, Yr = /[.+*?^${}()[\]/\\]/g;
function Gr(e, t) {
  const n = L({}, Kr, t), r = [];
  let s = n.start ? "^" : "";
  const o = [];
  for (const d of e) {
    const a = d.length ? [] : [
      90
      /* PathScore.Root */
    ];
    n.strict && !d.length && (s += "/");
    for (let u = 0; u < d.length; u++) {
      const g = d[u];
      let m = 40 + (n.sensitive ? 0.25 : 0);
      if (g.type === 0)
        u || (s += "/"), s += g.value.replace(Yr, "\\$&"), m += 40;
      else if (g.type === 1) {
        const { value: w, repeatable: _, optional: q, regexp: A } = g;
        o.push({
          name: w,
          repeatable: _,
          optional: q
        });
        const O = A || zt;
        if (O !== zt) {
          m += 10;
          try {
            new RegExp(`(${O})`);
          } catch (M) {
            throw new Error(`Invalid custom RegExp for param "${w}" (${O}): ` + M.message);
          }
        }
        let E = _ ? `((?:${O})(?:/(?:${O}))*)` : `(${O})`;
        u || (E = // avoid an optional / if there are more segments e.g. /:p?-static
        // or /:p?-:p2
        q && d.length < 2 ? `(?:/${E})` : "/" + E), q && (E += "?"), s += E, m += 20, q && (m += -8), _ && (m += -20), O === ".*" && (m += -50);
      }
      a.push(m);
    }
    r.push(a);
  }
  if (n.strict && n.end) {
    const d = r.length - 1;
    r[d][r[d].length - 1] += 0.7000000000000001;
  }
  n.strict || (s += "/?"), n.end ? s += "$" : n.strict && (s += "(?:/|$)");
  const i = new RegExp(s, n.sensitive ? "" : "i");
  function l(d) {
    const a = d.match(i), u = {};
    if (!a)
      return null;
    for (let g = 1; g < a.length; g++) {
      const m = a[g] || "", w = o[g - 1];
      u[w.name] = m && w.repeatable ? m.split("/") : m;
    }
    return u;
  }
  function c(d) {
    let a = "", u = !1;
    for (const g of e) {
      (!u || !a.endsWith("/")) && (a += "/"), u = !1;
      for (const m of g)
        if (m.type === 0)
          a += m.value;
        else if (m.type === 1) {
          const { value: w, repeatable: _, optional: q } = m, A = w in d ? d[w] : "";
          if (ne(A) && !_)
            throw new Error(`Provided param "${w}" is an array but it is not repeatable (* or + modifiers)`);
          const O = ne(A) ? A.join("/") : A;
          if (!O)
            if (q)
              g.length < 2 && (a.endsWith("/") ? a = a.slice(0, -1) : u = !0);
            else
              throw new Error(`Missing required param "${w}"`);
          a += O;
        }
    }
    return a || "/";
  }
  return {
    re: i,
    score: r,
    keys: o,
    parse: l,
    stringify: c
  };
}
function Jr(e, t) {
  let n = 0;
  for (; n < e.length && n < t.length; ) {
    const r = t[n] - e[n];
    if (r)
      return r;
    n++;
  }
  return e.length < t.length ? e.length === 1 && e[0] === 80 ? -1 : 1 : e.length > t.length ? t.length === 1 && t[0] === 80 ? 1 : -1 : 0;
}
function kn(e, t) {
  let n = 0;
  const r = e.score, s = t.score;
  for (; n < r.length && n < s.length; ) {
    const o = Jr(r[n], s[n]);
    if (o)
      return o;
    n++;
  }
  if (Math.abs(s.length - r.length) === 1) {
    if (Kt(r))
      return 1;
    if (Kt(s))
      return -1;
  }
  return s.length - r.length;
}
function Kt(e) {
  const t = e[e.length - 1];
  return e.length > 0 && t[t.length - 1] < 0;
}
const Qr = {
  type: 0,
  value: ""
}, Zr = /[a-zA-Z0-9_]/;
function Xr(e) {
  if (!e)
    return [[]];
  if (e === "/")
    return [[Qr]];
  if (!e.startsWith("/"))
    throw new Error(`Invalid path "${e}"`);
  function t(m) {
    throw new Error(`ERR (${n})/"${d}": ${m}`);
  }
  let n = 0, r = n;
  const s = [];
  let o;
  function i() {
    o && s.push(o), o = [];
  }
  let l = 0, c, d = "", a = "";
  function u() {
    d && (n === 0 ? o.push({
      type: 0,
      value: d
    }) : n === 1 || n === 2 || n === 3 ? (o.length > 1 && (c === "*" || c === "+") && t(`A repeatable param (${d}) must be alone in its segment. eg: '/:ids+.`), o.push({
      type: 1,
      value: d,
      regexp: a,
      repeatable: c === "*" || c === "+",
      optional: c === "*" || c === "?"
    })) : t("Invalid state to consume buffer"), d = "");
  }
  function g() {
    d += c;
  }
  for (; l < e.length; ) {
    if (c = e[l++], c === "\\" && n !== 2) {
      r = n, n = 4;
      continue;
    }
    switch (n) {
      case 0:
        c === "/" ? (d && u(), i()) : c === ":" ? (u(), n = 1) : g();
        break;
      case 4:
        g(), n = r;
        break;
      case 1:
        c === "(" ? n = 2 : Zr.test(c) ? g() : (u(), n = 0, c !== "*" && c !== "?" && c !== "+" && l--);
        break;
      case 2:
        c === ")" ? a[a.length - 1] == "\\" ? a = a.slice(0, -1) + c : n = 3 : a += c;
        break;
      case 3:
        u(), n = 0, c !== "*" && c !== "?" && c !== "+" && l--, a = "";
        break;
      default:
        t("Unknown state");
        break;
    }
  }
  return n === 2 && t(`Unfinished custom RegExp for param "${d}"`), u(), i(), s;
}
function es(e, t, n) {
  const r = Gr(Xr(e.path), n), s = L(r, {
    record: e,
    parent: t,
    // these needs to be populated by the parent
    children: [],
    alias: []
  });
  return t && !s.record.aliasOf == !t.record.aliasOf && t.children.push(s), s;
}
function ts(e, t) {
  const n = [], r = /* @__PURE__ */ new Map();
  t = Jt({ strict: !1, end: !0, sensitive: !1 }, t);
  function s(u) {
    return r.get(u);
  }
  function o(u, g, m) {
    const w = !m, _ = ns(u);
    _.aliasOf = m && m.record;
    const q = Jt(t, u), A = [
      _
    ];
    if ("alias" in u) {
      const M = typeof u.alias == "string" ? [u.alias] : u.alias;
      for (const Y of M)
        A.push(L({}, _, {
          // this allows us to hold a copy of the `components` option
          // so that async components cache is hold on the original record
          components: m ? m.record.components : _.components,
          path: Y,
          // we might be the child of an alias
          aliasOf: m ? m.record : _
          // the aliases are always of the same kind as the original since they
          // are defined on the same record
        }));
    }
    let O, E;
    for (const M of A) {
      const { path: Y } = M;
      if (g && Y[0] !== "/") {
        const $ = g.record.path, D = $[$.length - 1] === "/" ? "" : "/";
        M.path = g.record.path + (Y && D + Y);
      }
      if (O = es(M, g, q), m ? m.alias.push(O) : (E = E || O, E !== O && E.alias.push(O), w && u.name && !Gt(O) && i(u.name)), Cn(O) && c(O), _.children) {
        const $ = _.children;
        for (let D = 0; D < $.length; D++)
          o($[D], O, m && m.children[D]);
      }
      m = m || O;
    }
    return E ? () => {
      i(E);
    } : Ne;
  }
  function i(u) {
    if (An(u)) {
      const g = r.get(u);
      g && (r.delete(u), n.splice(n.indexOf(g), 1), g.children.forEach(i), g.alias.forEach(i));
    } else {
      const g = n.indexOf(u);
      g > -1 && (n.splice(g, 1), u.record.name && r.delete(u.record.name), u.children.forEach(i), u.alias.forEach(i));
    }
  }
  function l() {
    return n;
  }
  function c(u) {
    const g = os(u, n);
    n.splice(g, 0, u), u.record.name && !Gt(u) && r.set(u.record.name, u);
  }
  function d(u, g) {
    let m, w = {}, _, q;
    if ("name" in u && u.name) {
      if (m = r.get(u.name), !m)
        throw xe(1, {
          location: u
        });
      q = m.record.name, w = L(
        // paramsFromLocation is a new object
        Yt(
          g.params,
          // only keep params that exist in the resolved location
          // only keep optional params coming from a parent record
          m.keys.filter((E) => !E.optional).concat(m.parent ? m.parent.keys.filter((E) => E.optional) : []).map((E) => E.name)
        ),
        // discard any existing params in the current location that do not exist here
        // #1497 this ensures better active/exact matching
        u.params && Yt(u.params, m.keys.map((E) => E.name))
      ), _ = m.stringify(w);
    } else if (u.path != null)
      _ = u.path, m = n.find((E) => E.re.test(_)), m && (w = m.parse(_), q = m.record.name);
    else {
      if (m = g.name ? r.get(g.name) : n.find((E) => E.re.test(g.path)), !m)
        throw xe(1, {
          location: u,
          currentLocation: g
        });
      q = m.record.name, w = L({}, g.params, u.params), _ = m.stringify(w);
    }
    const A = [];
    let O = m;
    for (; O; )
      A.unshift(O.record), O = O.parent;
    return {
      name: q,
      path: _,
      params: w,
      matched: A,
      meta: ss(A)
    };
  }
  e.forEach((u) => o(u));
  function a() {
    n.length = 0, r.clear();
  }
  return {
    addRoute: o,
    resolve: d,
    removeRoute: i,
    clearRoutes: a,
    getRoutes: l,
    getRecordMatcher: s
  };
}
function Yt(e, t) {
  const n = {};
  for (const r of t)
    r in e && (n[r] = e[r]);
  return n;
}
function ns(e) {
  return {
    path: e.path,
    redirect: e.redirect,
    name: e.name,
    meta: e.meta || {},
    aliasOf: void 0,
    beforeEnter: e.beforeEnter,
    props: rs(e),
    children: e.children || [],
    instances: {},
    leaveGuards: /* @__PURE__ */ new Set(),
    updateGuards: /* @__PURE__ */ new Set(),
    enterCallbacks: {},
    components: "components" in e ? e.components || null : e.component && { default: e.component }
  };
}
function rs(e) {
  const t = {}, n = e.props || !1;
  if ("component" in e)
    t.default = n;
  else
    for (const r in e.components)
      t[r] = typeof n == "object" ? n[r] : n;
  return t;
}
function Gt(e) {
  for (; e; ) {
    if (e.record.aliasOf)
      return !0;
    e = e.parent;
  }
  return !1;
}
function ss(e) {
  return e.reduce((t, n) => L(t, n.meta), {});
}
function Jt(e, t) {
  const n = {};
  for (const r in e)
    n[r] = r in t ? t[r] : e[r];
  return n;
}
function os(e, t) {
  let n = 0, r = t.length;
  for (; n !== r; ) {
    const o = n + r >> 1;
    kn(e, t[o]) < 0 ? r = o : n = o + 1;
  }
  const s = is(e);
  return s && (r = t.lastIndexOf(s, r - 1)), r;
}
function is(e) {
  let t = e;
  for (; t = t.parent; )
    if (Cn(t) && kn(e, t) === 0)
      return t;
}
function Cn({ record: e }) {
  return !!(e.name || e.components && Object.keys(e.components).length || e.redirect);
}
function as(e) {
  const t = {};
  if (e === "" || e === "?")
    return t;
  const r = (e[0] === "?" ? e.slice(1) : e).split("&");
  for (let s = 0; s < r.length; ++s) {
    const o = r[s].replace(En, " "), i = o.indexOf("="), l = Fe(i < 0 ? o : o.slice(0, i)), c = i < 0 ? null : Fe(o.slice(i + 1));
    if (l in t) {
      let d = t[l];
      ne(d) || (d = t[l] = [d]), d.push(c);
    } else
      t[l] = c;
  }
  return t;
}
function Qt(e) {
  let t = "";
  for (let n in e) {
    const r = e[n];
    if (n = Sr(n), r == null) {
      r !== void 0 && (t += (t.length ? "&" : "") + n);
      continue;
    }
    (ne(r) ? r.map((o) => o && _t(o)) : [r && _t(r)]).forEach((o) => {
      o !== void 0 && (t += (t.length ? "&" : "") + n, o != null && (t += "=" + o));
    });
  }
  return t;
}
function ls(e) {
  const t = {};
  for (const n in e) {
    const r = e[n];
    r !== void 0 && (t[n] = ne(r) ? r.map((s) => s == null ? null : "" + s) : r == null ? r : "" + r);
  }
  return t;
}
const cs = Symbol(""), Zt = Symbol(""), kt = Symbol(""), Pn = Symbol(""), vt = Symbol("");
function Ae() {
  let e = [];
  function t(r) {
    return e.push(r), () => {
      const s = e.indexOf(r);
      s > -1 && e.splice(s, 1);
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
function me(e, t, n, r, s, o = (i) => i()) {
  const i = r && // name is defined if record is because of the function overload
  (r.enterCallbacks[s] = r.enterCallbacks[s] || []);
  return () => new Promise((l, c) => {
    const d = (g) => {
      g === !1 ? c(xe(4, {
        from: n,
        to: t
      })) : g instanceof Error ? c(g) : zr(g) ? c(xe(2, {
        from: t,
        to: g
      })) : (i && // since enterCallbackArray is truthy, both record and name also are
      r.enterCallbacks[s] === i && typeof g == "function" && i.push(g), l());
    }, a = o(() => e.call(r && r.instances[s], t, n, d));
    let u = Promise.resolve(a);
    e.length < 3 && (u = u.then(d)), u.catch((g) => c(g));
  });
}
function ht(e, t, n, r, s = (o) => o()) {
  const o = [];
  for (const i of e)
    for (const l in i.components) {
      let c = i.components[l];
      if (!(t !== "beforeRouteEnter" && !i.instances[l]))
        if (us(c)) {
          const a = (c.__vccOpts || c)[t];
          a && o.push(me(a, n, r, i, l, s));
        } else {
          let d = c();
          o.push(() => d.then((a) => {
            if (!a)
              return Promise.reject(new Error(`Couldn't resolve component "${l}" at "${i.path}"`));
            const u = pr(a) ? a.default : a;
            i.components[l] = u;
            const m = (u.__vccOpts || u)[t];
            return m && me(m, n, r, i, l, s)();
          }));
        }
    }
  return o;
}
function us(e) {
  return typeof e == "object" || "displayName" in e || "props" in e || "__vccOpts" in e;
}
function Xt(e) {
  const t = Le(kt), n = Le(Pn), r = ie(() => {
    const c = Pe(e.to);
    return t.resolve(c);
  }), s = ie(() => {
    const { matched: c } = r.value, { length: d } = c, a = c[d - 1], u = n.matched;
    if (!a || !u.length)
      return -1;
    const g = u.findIndex(Re.bind(null, a));
    if (g > -1)
      return g;
    const m = en(c[d - 2]);
    return (
      // we are dealing with nested routes
      d > 1 && // if the parent and matched route have the same path, this link is
      // referring to the empty child. Or we currently are on a different
      // child of the same parent
      en(a) === m && // avoid comparing the child with its parent
      u[u.length - 1].path !== m ? u.findIndex(Re.bind(null, c[d - 2])) : g
    );
  }), o = ie(() => s.value > -1 && ps(n.params, r.value.params)), i = ie(() => s.value > -1 && s.value === n.matched.length - 1 && On(n.params, r.value.params));
  function l(c = {}) {
    return hs(c) ? t[Pe(e.replace) ? "replace" : "push"](
      Pe(e.to)
      // avoid uncaught errors are they are logged anyway
    ).catch(Ne) : Promise.resolve();
  }
  return {
    route: r,
    href: ie(() => r.value.href),
    isActive: o,
    isExactActive: i,
    navigate: l
  };
}
const ds = /* @__PURE__ */ _n({
  name: "RouterLink",
  compatConfig: { MODE: 3 },
  props: {
    to: {
      type: [String, Object],
      required: !0
    },
    replace: Boolean,
    activeClass: String,
    // inactiveClass: String,
    exactActiveClass: String,
    custom: Boolean,
    ariaCurrentValue: {
      type: String,
      default: "page"
    }
  },
  useLink: Xt,
  setup(e, { slots: t }) {
    const n = lr(Xt(e)), { options: r } = Le(kt), s = ie(() => ({
      [tn(e.activeClass, r.linkActiveClass, "router-link-active")]: n.isActive,
      // [getLinkClass(
      //   props.inactiveClass,
      //   options.linkInactiveClass,
      //   'router-link-inactive'
      // )]: !link.isExactActive,
      [tn(e.exactActiveClass, r.linkExactActiveClass, "router-link-exact-active")]: n.isExactActive
    }));
    return () => {
      const o = t.default && t.default(n);
      return e.custom ? o : bn("a", {
        "aria-current": n.isExactActive ? e.ariaCurrentValue : null,
        href: n.href,
        // this would override user added attrs but Vue will still add
        // the listener, so we end up triggering both
        onClick: n.navigate,
        class: s.value
      }, o);
    };
  }
}), fs = ds;
function hs(e) {
  if (!(e.metaKey || e.altKey || e.ctrlKey || e.shiftKey) && !e.defaultPrevented && !(e.button !== void 0 && e.button !== 0)) {
    if (e.currentTarget && e.currentTarget.getAttribute) {
      const t = e.currentTarget.getAttribute("target");
      if (/\b_blank\b/i.test(t))
        return;
    }
    return e.preventDefault && e.preventDefault(), !0;
  }
}
function ps(e, t) {
  for (const n in t) {
    const r = t[n], s = e[n];
    if (typeof r == "string") {
      if (r !== s)
        return !1;
    } else if (!ne(s) || s.length !== r.length || r.some((o, i) => o !== s[i]))
      return !1;
  }
  return !0;
}
function en(e) {
  return e ? e.aliasOf ? e.aliasOf.path : e.path : "";
}
const tn = (e, t, n) => e ?? t ?? n, ms = /* @__PURE__ */ _n({
  name: "RouterView",
  // #674 we manually inherit them
  inheritAttrs: !1,
  props: {
    name: {
      type: String,
      default: "default"
    },
    route: Object
  },
  // Better compat for @vue/compat users
  // https://github.com/vuejs/router/issues/1315
  compatConfig: { MODE: 3 },
  setup(e, { attrs: t, slots: n }) {
    const r = Le(vt), s = ie(() => e.route || r.value), o = Le(Zt, 0), i = ie(() => {
      let d = Pe(o);
      const { matched: a } = s.value;
      let u;
      for (; (u = a[d]) && !u.components; )
        d++;
      return d;
    }), l = ie(() => s.value.matched[i.value]);
    ut(Zt, ie(() => i.value + 1)), ut(cs, l), ut(vt, s);
    const c = cr();
    return ur(() => [c.value, l.value, e.name], ([d, a, u], [g, m, w]) => {
      a && (a.instances[u] = d, m && m !== a && d && d === g && (a.leaveGuards.size || (a.leaveGuards = m.leaveGuards), a.updateGuards.size || (a.updateGuards = m.updateGuards))), d && a && // if there is no instance but to and from are the same this might be
      // the first visit
      (!m || !Re(a, m) || !g) && (a.enterCallbacks[u] || []).forEach((_) => _(d));
    }, { flush: "post" }), () => {
      const d = s.value, a = e.name, u = l.value, g = u && u.components[a];
      if (!g)
        return nn(n.default, { Component: g, route: d });
      const m = u.props[a], w = m ? m === !0 ? d.params : typeof m == "function" ? m(d) : m : null, q = bn(g, L({}, w, t, {
        onVnodeUnmounted: (A) => {
          A.component.isUnmounted && (u.instances[a] = null);
        },
        ref: c
      }));
      return (
        // pass the vnode to the slot as a prop.
        // h and <component :is="..."> both accept vnodes
        nn(n.default, { Component: q, route: d }) || q
      );
    };
  }
});
function nn(e, t) {
  if (!e)
    return null;
  const n = e(t);
  return n.length === 1 ? n[0] : n;
}
const gs = ms;
function ws(e) {
  const t = ts(e.routes, e), n = e.parseQuery || as, r = e.stringifyQuery || Qt, s = e.history, o = Ae(), i = Ae(), l = Ae(), c = dr(ue);
  let d = ue;
  ve && e.scrollBehavior && "scrollRestoration" in history && (history.scrollRestoration = "manual");
  const a = dt.bind(null, (y) => "" + y), u = dt.bind(null, qr), g = (
    // @ts-expect-error: intentionally avoid the type check
    dt.bind(null, Fe)
  );
  function m(y, v) {
    let b, x;
    return An(y) ? (b = t.getRecordMatcher(y), x = v) : x = y, t.addRoute(x, b);
  }
  function w(y) {
    const v = t.getRecordMatcher(y);
    v && t.removeRoute(v);
  }
  function _() {
    return t.getRoutes().map((y) => y.record);
  }
  function q(y) {
    return !!t.getRecordMatcher(y);
  }
  function A(y, v) {
    if (v = L({}, v || c.value), typeof y == "string") {
      const k = ft(n, y, v.path), W = t.resolve({ path: k.path }, v), qe = s.createHref(k.fullPath);
      return L(k, W, {
        params: g(W.params),
        hash: Fe(k.hash),
        redirectedFrom: void 0,
        href: qe
      });
    }
    let b;
    if (y.path != null)
      b = L({}, y, {
        path: ft(n, y.path, v.path).path
      });
    else {
      const k = L({}, y.params);
      for (const W in k)
        k[W] == null && delete k[W];
      b = L({}, y, {
        params: u(k)
      }), v.params = u(v.params);
    }
    const x = t.resolve(b, v), B = y.hash || "";
    x.params = a(g(x.params));
    const j = kr(r, L({}, y, {
      hash: xr(B),
      path: x.path
    })), N = s.createHref(j);
    return L({
      fullPath: j,
      // keep the hash encoded so fullPath is effectively path + encodedQuery +
      // hash
      hash: B,
      query: (
        // if the user is using a custom query lib like qs, we might have
        // nested objects, so we keep the query as is, meaning it can contain
        // numbers at `$route.query`, but at the point, the user will have to
        // use their own type anyway.
        // https://github.com/vuejs/router/issues/328#issuecomment-649481567
        r === Qt ? ls(y.query) : y.query || {}
      )
    }, x, {
      redirectedFrom: void 0,
      href: N
    });
  }
  function O(y) {
    return typeof y == "string" ? ft(n, y, c.value.path) : L({}, y);
  }
  function E(y, v) {
    if (d !== y)
      return xe(8, {
        from: v,
        to: y
      });
  }
  function M(y) {
    return D(y);
  }
  function Y(y) {
    return M(L(O(y), { replace: !0 }));
  }
  function $(y) {
    const v = y.matched[y.matched.length - 1];
    if (v && v.redirect) {
      const { redirect: b } = v;
      let x = typeof b == "function" ? b(y) : b;
      return typeof x == "string" && (x = x.includes("?") || x.includes("#") ? x = O(x) : (
        // force empty params
        { path: x }
      ), x.params = {}), L({
        query: y.query,
        hash: y.hash,
        // avoid transferring params if the redirect has a path
        params: x.path != null ? {} : y.params
      }, x);
    }
  }
  function D(y, v) {
    const b = d = A(y), x = c.value, B = y.state, j = y.force, N = y.replace === !0, k = $(b);
    if (k)
      return D(
        L(O(k), {
          state: typeof k == "object" ? L({}, B, k.state) : B,
          force: j,
          replace: N
        }),
        // keep original redirectedFrom if it exists
        v || b
      );
    const W = b;
    W.redirectedFrom = v;
    let qe;
    return !j && Cr(r, x, b) && (qe = xe(16, { to: W, from: x }), Dt(
      x,
      x,
      // this is a push, the only way for it to be triggered from a
      // history.listen is with a redirect, which makes it become a push
      !0,
      // This cannot be the first navigation because the initial location
      // cannot be manually navigated to
      !1
    )), (qe ? Promise.resolve(qe) : $e(W, x)).catch((G) => ae(G) ? (
      // navigation redirects still mark the router as ready
      ae(
        G,
        2
        /* ErrorTypes.NAVIGATION_GUARD_REDIRECT */
      ) ? G : at(G)
    ) : (
      // reject any unknown error
      it(G, W, x)
    )).then((G) => {
      if (G) {
        if (ae(
          G,
          2
          /* ErrorTypes.NAVIGATION_GUARD_REDIRECT */
        ))
          return D(
            // keep options
            L({
              // preserve an existing replacement but allow the redirect to override it
              replace: N
            }, O(G.to), {
              state: typeof G.to == "object" ? L({}, B, G.to.state) : B,
              force: j
            }),
            // preserve the original redirectedFrom if any
            v || W
          );
      } else
        G = P(W, x, !0, N, B);
      return S(W, x, G), G;
    });
  }
  function ye(y, v) {
    const b = E(y, v);
    return b ? Promise.reject(b) : Promise.resolve();
  }
  function _e(y) {
    const v = We.values().next().value;
    return v && typeof v.runWithContext == "function" ? v.runWithContext(y) : y();
  }
  function $e(y, v) {
    let b;
    const [x, B, j] = ys(y, v);
    b = ht(x.reverse(), "beforeRouteLeave", y, v);
    for (const k of x)
      k.leaveGuards.forEach((W) => {
        b.push(me(W, y, v));
      });
    const N = ye.bind(null, y, v);
    return b.push(N), be(b).then(() => {
      b = [];
      for (const k of o.list())
        b.push(me(k, y, v));
      return b.push(N), be(b);
    }).then(() => {
      b = ht(B, "beforeRouteUpdate", y, v);
      for (const k of B)
        k.updateGuards.forEach((W) => {
          b.push(me(W, y, v));
        });
      return b.push(N), be(b);
    }).then(() => {
      b = [];
      for (const k of j)
        if (k.beforeEnter)
          if (ne(k.beforeEnter))
            for (const W of k.beforeEnter)
              b.push(me(W, y, v));
          else
            b.push(me(k.beforeEnter, y, v));
      return b.push(N), be(b);
    }).then(() => (y.matched.forEach((k) => k.enterCallbacks = {}), b = ht(j, "beforeRouteEnter", y, v, _e), b.push(N), be(b))).then(() => {
      b = [];
      for (const k of i.list())
        b.push(me(k, y, v));
      return b.push(N), be(b);
    }).catch((k) => ae(
      k,
      8
      /* ErrorTypes.NAVIGATION_CANCELLED */
    ) ? k : Promise.reject(k));
  }
  function S(y, v, b) {
    l.list().forEach((x) => _e(() => x(y, v, b)));
  }
  function P(y, v, b, x, B) {
    const j = E(y, v);
    if (j)
      return j;
    const N = v === ue, k = ve ? history.state : {};
    b && (x || N ? s.replace(y.fullPath, L({
      scroll: N && k && k.scroll
    }, B)) : s.push(y.fullPath, B)), c.value = y, Dt(y, v, b, N), at();
  }
  let se;
  function ir() {
    se || (se = s.listen((y, v, b) => {
      if (!It.listening)
        return;
      const x = A(y), B = $(x);
      if (B) {
        D(L(B, { replace: !0 }), x).catch(Ne);
        return;
      }
      d = x;
      const j = c.value;
      ve && Dr(jt(j.fullPath, b.delta), Xe()), $e(x, j).catch((N) => ae(
        N,
        12
        /* ErrorTypes.NAVIGATION_CANCELLED */
      ) ? N : ae(
        N,
        2
        /* ErrorTypes.NAVIGATION_GUARD_REDIRECT */
      ) ? (D(
        N.to,
        x
        // avoid an uncaught rejection, let push call triggerError
      ).then((k) => {
        ae(
          k,
          20
          /* ErrorTypes.NAVIGATION_DUPLICATED */
        ) && !b.delta && b.type === Ue.pop && s.go(-1, !1);
      }).catch(Ne), Promise.reject()) : (b.delta && s.go(-b.delta, !1), it(N, x, j))).then((N) => {
        N = N || P(
          // after navigation, all matched components are resolved
          x,
          j,
          !1
        ), N && (b.delta && // a new navigation has been triggered, so we do not want to revert, that will change the current history
        // entry while a different route is displayed
        !ae(
          N,
          8
          /* ErrorTypes.NAVIGATION_CANCELLED */
        ) ? s.go(-b.delta, !1) : b.type === Ue.pop && ae(
          N,
          20
          /* ErrorTypes.NAVIGATION_DUPLICATED */
        ) && s.go(-1, !1)), S(x, j, N);
      }).catch(Ne);
    }));
  }
  let ot = Ae(), Bt = Ae(), je;
  function it(y, v, b) {
    at(y);
    const x = Bt.list();
    return x.length ? x.forEach((B) => B(y, v, b)) : console.error(y), Promise.reject(y);
  }
  function ar() {
    return je && c.value !== ue ? Promise.resolve() : new Promise((y, v) => {
      ot.add([y, v]);
    });
  }
  function at(y) {
    return je || (je = !y, ir(), ot.list().forEach(([v, b]) => y ? b(y) : v()), ot.reset()), y;
  }
  function Dt(y, v, b, x) {
    const { scrollBehavior: B } = e;
    if (!ve || !B)
      return Promise.resolve();
    const j = !b && Ir(jt(y.fullPath, 0)) || (x || !b) && history.state && history.state.scroll || null;
    return hr().then(() => B(y, v, j)).then((N) => N && Br(N)).catch((N) => it(N, y, v));
  }
  const lt = (y) => s.go(y);
  let ct;
  const We = /* @__PURE__ */ new Set(), It = {
    currentRoute: c,
    listening: !0,
    addRoute: m,
    removeRoute: w,
    clearRoutes: t.clearRoutes,
    hasRoute: q,
    getRoutes: _,
    resolve: A,
    options: e,
    push: M,
    replace: Y,
    go: lt,
    back: () => lt(-1),
    forward: () => lt(1),
    beforeEach: o.add,
    beforeResolve: i.add,
    afterEach: l.add,
    onError: Bt.add,
    isReady: ar,
    install(y) {
      const v = this;
      y.component("RouterLink", fs), y.component("RouterView", gs), y.config.globalProperties.$router = v, Object.defineProperty(y.config.globalProperties, "$route", {
        enumerable: !0,
        get: () => Pe(c)
      }), ve && // used for the initial navigation client side to avoid pushing
      // multiple times when the router is used in multiple apps
      !ct && c.value === ue && (ct = !0, M(s.location).catch((B) => {
      }));
      const b = {};
      for (const B in ue)
        Object.defineProperty(b, B, {
          get: () => c.value[B],
          enumerable: !0
        });
      y.provide(kt, v), y.provide(Pn, fr(b)), y.provide(vt, c);
      const x = y.unmount;
      We.add(y), y.unmount = function() {
        We.delete(y), We.size < 1 && (d = ue, se && se(), se = null, c.value = ue, ct = !1, je = !1), x();
      };
    }
  };
  function be(y) {
    return y.reduce((v, b) => v.then(() => _e(b)), Promise.resolve());
  }
  return It;
}
function ys(e, t) {
  const n = [], r = [], s = [], o = Math.max(t.matched.length, e.matched.length);
  for (let i = 0; i < o; i++) {
    const l = t.matched[i];
    l && (e.matched.find((d) => Re(d, l)) ? r.push(l) : n.push(l));
    const c = e.matched[i];
    c && (t.matched.find((d) => Re(d, c)) || s.push(c));
  }
  return [n, r, s];
}
function Nn(e, t) {
  return function() {
    return e.apply(t, arguments);
  };
}
const { toString: _s } = Object.prototype, { getPrototypeOf: Ct } = Object, et = /* @__PURE__ */ ((e) => (t) => {
  const n = _s.call(t);
  return e[n] || (e[n] = n.slice(8, -1).toLowerCase());
})(/* @__PURE__ */ Object.create(null)), re = (e) => (e = e.toLowerCase(), (t) => et(t) === e), tt = (e) => (t) => typeof t === e, { isArray: Se } = Array, Be = tt("undefined");
function bs(e) {
  return e !== null && !Be(e) && e.constructor !== null && !Be(e.constructor) && X(e.constructor.isBuffer) && e.constructor.isBuffer(e);
}
const Vn = re("ArrayBuffer");
function vs(e) {
  let t;
  return typeof ArrayBuffer < "u" && ArrayBuffer.isView ? t = ArrayBuffer.isView(e) : t = e && e.buffer && Vn(e.buffer), t;
}
const Es = tt("string"), X = tt("function"), Ln = tt("number"), nt = (e) => e !== null && typeof e == "object", Rs = (e) => e === !0 || e === !1, Ye = (e) => {
  if (et(e) !== "object")
    return !1;
  const t = Ct(e);
  return (t === null || t === Object.prototype || Object.getPrototypeOf(t) === null) && !(Symbol.toStringTag in e) && !(Symbol.iterator in e);
}, xs = re("Date"), Ss = re("File"), Os = re("Blob"), qs = re("FileList"), As = (e) => nt(e) && X(e.pipe), Ts = (e) => {
  let t;
  return e && (typeof FormData == "function" && e instanceof FormData || X(e.append) && ((t = et(e)) === "formdata" || // detect form-data instance
  t === "object" && X(e.toString) && e.toString() === "[object FormData]"));
}, ks = re("URLSearchParams"), [Cs, Ps, Ns, Vs] = ["ReadableStream", "Request", "Response", "Headers"].map(re), Ls = (e) => e.trim ? e.trim() : e.replace(/^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g, "");
function De(e, t, { allOwnKeys: n = !1 } = {}) {
  if (e === null || typeof e > "u")
    return;
  let r, s;
  if (typeof e != "object" && (e = [e]), Se(e))
    for (r = 0, s = e.length; r < s; r++)
      t.call(null, e[r], r, e);
  else {
    const o = n ? Object.getOwnPropertyNames(e) : Object.keys(e), i = o.length;
    let l;
    for (r = 0; r < i; r++)
      l = o[r], t.call(null, e[l], l, e);
  }
}
function Fn(e, t) {
  t = t.toLowerCase();
  const n = Object.keys(e);
  let r = n.length, s;
  for (; r-- > 0; )
    if (s = n[r], t === s.toLowerCase())
      return s;
  return null;
}
const Un = typeof globalThis < "u" ? globalThis : typeof self < "u" ? self : typeof window < "u" ? window : global, Bn = (e) => !Be(e) && e !== Un;
function Et() {
  const { caseless: e } = Bn(this) && this || {}, t = {}, n = (r, s) => {
    const o = e && Fn(t, s) || s;
    Ye(t[o]) && Ye(r) ? t[o] = Et(t[o], r) : Ye(r) ? t[o] = Et({}, r) : Se(r) ? t[o] = r.slice() : t[o] = r;
  };
  for (let r = 0, s = arguments.length; r < s; r++)
    arguments[r] && De(arguments[r], n);
  return t;
}
const Fs = (e, t, n, { allOwnKeys: r } = {}) => (De(t, (s, o) => {
  n && X(s) ? e[o] = Nn(s, n) : e[o] = s;
}, { allOwnKeys: r }), e), Us = (e) => (e.charCodeAt(0) === 65279 && (e = e.slice(1)), e), Bs = (e, t, n, r) => {
  e.prototype = Object.create(t.prototype, r), e.prototype.constructor = e, Object.defineProperty(e, "super", {
    value: t.prototype
  }), n && Object.assign(e.prototype, n);
}, Ds = (e, t, n, r) => {
  let s, o, i;
  const l = {};
  if (t = t || {}, e == null) return t;
  do {
    for (s = Object.getOwnPropertyNames(e), o = s.length; o-- > 0; )
      i = s[o], (!r || r(i, e, t)) && !l[i] && (t[i] = e[i], l[i] = !0);
    e = n !== !1 && Ct(e);
  } while (e && (!n || n(e, t)) && e !== Object.prototype);
  return t;
}, Is = (e, t, n) => {
  e = String(e), (n === void 0 || n > e.length) && (n = e.length), n -= t.length;
  const r = e.indexOf(t, n);
  return r !== -1 && r === n;
}, Ms = (e) => {
  if (!e) return null;
  if (Se(e)) return e;
  let t = e.length;
  if (!Ln(t)) return null;
  const n = new Array(t);
  for (; t-- > 0; )
    n[t] = e[t];
  return n;
}, $s = /* @__PURE__ */ ((e) => (t) => e && t instanceof e)(typeof Uint8Array < "u" && Ct(Uint8Array)), js = (e, t) => {
  const r = (e && e[Symbol.iterator]).call(e);
  let s;
  for (; (s = r.next()) && !s.done; ) {
    const o = s.value;
    t.call(e, o[0], o[1]);
  }
}, Ws = (e, t) => {
  let n;
  const r = [];
  for (; (n = e.exec(t)) !== null; )
    r.push(n);
  return r;
}, Hs = re("HTMLFormElement"), zs = (e) => e.toLowerCase().replace(
  /[-_\s]([a-z\d])(\w*)/g,
  function(n, r, s) {
    return r.toUpperCase() + s;
  }
), rn = (({ hasOwnProperty: e }) => (t, n) => e.call(t, n))(Object.prototype), Ks = re("RegExp"), Dn = (e, t) => {
  const n = Object.getOwnPropertyDescriptors(e), r = {};
  De(n, (s, o) => {
    let i;
    (i = t(s, o, e)) !== !1 && (r[o] = i || s);
  }), Object.defineProperties(e, r);
}, Ys = (e) => {
  Dn(e, (t, n) => {
    if (X(e) && ["arguments", "caller", "callee"].indexOf(n) !== -1)
      return !1;
    const r = e[n];
    if (X(r)) {
      if (t.enumerable = !1, "writable" in t) {
        t.writable = !1;
        return;
      }
      t.set || (t.set = () => {
        throw Error("Can not rewrite read-only method '" + n + "'");
      });
    }
  });
}, Gs = (e, t) => {
  const n = {}, r = (s) => {
    s.forEach((o) => {
      n[o] = !0;
    });
  };
  return Se(e) ? r(e) : r(String(e).split(t)), n;
}, Js = () => {
}, Qs = (e, t) => e != null && Number.isFinite(e = +e) ? e : t, pt = "abcdefghijklmnopqrstuvwxyz", sn = "0123456789", In = {
  DIGIT: sn,
  ALPHA: pt,
  ALPHA_DIGIT: pt + pt.toUpperCase() + sn
}, Zs = (e = 16, t = In.ALPHA_DIGIT) => {
  let n = "";
  const { length: r } = t;
  for (; e--; )
    n += t[Math.random() * r | 0];
  return n;
};
function Xs(e) {
  return !!(e && X(e.append) && e[Symbol.toStringTag] === "FormData" && e[Symbol.iterator]);
}
const eo = (e) => {
  const t = new Array(10), n = (r, s) => {
    if (nt(r)) {
      if (t.indexOf(r) >= 0)
        return;
      if (!("toJSON" in r)) {
        t[s] = r;
        const o = Se(r) ? [] : {};
        return De(r, (i, l) => {
          const c = n(i, s + 1);
          !Be(c) && (o[l] = c);
        }), t[s] = void 0, o;
      }
    }
    return r;
  };
  return n(e, 0);
}, to = re("AsyncFunction"), no = (e) => e && (nt(e) || X(e)) && X(e.then) && X(e.catch), h = {
  isArray: Se,
  isArrayBuffer: Vn,
  isBuffer: bs,
  isFormData: Ts,
  isArrayBufferView: vs,
  isString: Es,
  isNumber: Ln,
  isBoolean: Rs,
  isObject: nt,
  isPlainObject: Ye,
  isReadableStream: Cs,
  isRequest: Ps,
  isResponse: Ns,
  isHeaders: Vs,
  isUndefined: Be,
  isDate: xs,
  isFile: Ss,
  isBlob: Os,
  isRegExp: Ks,
  isFunction: X,
  isStream: As,
  isURLSearchParams: ks,
  isTypedArray: $s,
  isFileList: qs,
  forEach: De,
  merge: Et,
  extend: Fs,
  trim: Ls,
  stripBOM: Us,
  inherits: Bs,
  toFlatObject: Ds,
  kindOf: et,
  kindOfTest: re,
  endsWith: Is,
  toArray: Ms,
  forEachEntry: js,
  matchAll: Ws,
  isHTMLForm: Hs,
  hasOwnProperty: rn,
  hasOwnProp: rn,
  // an alias to avoid ESLint no-prototype-builtins detection
  reduceDescriptors: Dn,
  freezeMethods: Ys,
  toObjectSet: Gs,
  toCamelCase: zs,
  noop: Js,
  toFiniteNumber: Qs,
  findKey: Fn,
  global: Un,
  isContextDefined: Bn,
  ALPHABET: In,
  generateString: Zs,
  isSpecCompliantForm: Xs,
  toJSONObject: eo,
  isAsyncFn: to,
  isThenable: no
};
function T(e, t, n, r, s) {
  Error.call(this), Error.captureStackTrace ? Error.captureStackTrace(this, this.constructor) : this.stack = new Error().stack, this.message = e, this.name = "AxiosError", t && (this.code = t), n && (this.config = n), r && (this.request = r), s && (this.response = s);
}
h.inherits(T, Error, {
  toJSON: function() {
    return {
      // Standard
      message: this.message,
      name: this.name,
      // Microsoft
      description: this.description,
      number: this.number,
      // Mozilla
      fileName: this.fileName,
      lineNumber: this.lineNumber,
      columnNumber: this.columnNumber,
      stack: this.stack,
      // Axios
      config: h.toJSONObject(this.config),
      code: this.code,
      status: this.response && this.response.status ? this.response.status : null
    };
  }
});
const Mn = T.prototype, $n = {};
[
  "ERR_BAD_OPTION_VALUE",
  "ERR_BAD_OPTION",
  "ECONNABORTED",
  "ETIMEDOUT",
  "ERR_NETWORK",
  "ERR_FR_TOO_MANY_REDIRECTS",
  "ERR_DEPRECATED",
  "ERR_BAD_RESPONSE",
  "ERR_BAD_REQUEST",
  "ERR_CANCELED",
  "ERR_NOT_SUPPORT",
  "ERR_INVALID_URL"
  // eslint-disable-next-line func-names
].forEach((e) => {
  $n[e] = { value: e };
});
Object.defineProperties(T, $n);
Object.defineProperty(Mn, "isAxiosError", { value: !0 });
T.from = (e, t, n, r, s, o) => {
  const i = Object.create(Mn);
  return h.toFlatObject(e, i, function(c) {
    return c !== Error.prototype;
  }, (l) => l !== "isAxiosError"), T.call(i, e.message, t, n, r, s), i.cause = e, i.name = e.name, o && Object.assign(i, o), i;
};
const ro = null;
function Rt(e) {
  return h.isPlainObject(e) || h.isArray(e);
}
function jn(e) {
  return h.endsWith(e, "[]") ? e.slice(0, -2) : e;
}
function on(e, t, n) {
  return e ? e.concat(t).map(function(s, o) {
    return s = jn(s), !n && o ? "[" + s + "]" : s;
  }).join(n ? "." : "") : t;
}
function so(e) {
  return h.isArray(e) && !e.some(Rt);
}
const oo = h.toFlatObject(h, {}, null, function(t) {
  return /^is[A-Z]/.test(t);
});
function rt(e, t, n) {
  if (!h.isObject(e))
    throw new TypeError("target must be an object");
  t = t || new FormData(), n = h.toFlatObject(n, {
    metaTokens: !0,
    dots: !1,
    indexes: !1
  }, !1, function(_, q) {
    return !h.isUndefined(q[_]);
  });
  const r = n.metaTokens, s = n.visitor || a, o = n.dots, i = n.indexes, c = (n.Blob || typeof Blob < "u" && Blob) && h.isSpecCompliantForm(t);
  if (!h.isFunction(s))
    throw new TypeError("visitor must be a function");
  function d(w) {
    if (w === null) return "";
    if (h.isDate(w))
      return w.toISOString();
    if (!c && h.isBlob(w))
      throw new T("Blob is not supported. Use a Buffer instead.");
    return h.isArrayBuffer(w) || h.isTypedArray(w) ? c && typeof Blob == "function" ? new Blob([w]) : Buffer.from(w) : w;
  }
  function a(w, _, q) {
    let A = w;
    if (w && !q && typeof w == "object") {
      if (h.endsWith(_, "{}"))
        _ = r ? _ : _.slice(0, -2), w = JSON.stringify(w);
      else if (h.isArray(w) && so(w) || (h.isFileList(w) || h.endsWith(_, "[]")) && (A = h.toArray(w)))
        return _ = jn(_), A.forEach(function(E, M) {
          !(h.isUndefined(E) || E === null) && t.append(
            // eslint-disable-next-line no-nested-ternary
            i === !0 ? on([_], M, o) : i === null ? _ : _ + "[]",
            d(E)
          );
        }), !1;
    }
    return Rt(w) ? !0 : (t.append(on(q, _, o), d(w)), !1);
  }
  const u = [], g = Object.assign(oo, {
    defaultVisitor: a,
    convertValue: d,
    isVisitable: Rt
  });
  function m(w, _) {
    if (!h.isUndefined(w)) {
      if (u.indexOf(w) !== -1)
        throw Error("Circular reference detected in " + _.join("."));
      u.push(w), h.forEach(w, function(A, O) {
        (!(h.isUndefined(A) || A === null) && s.call(
          t,
          A,
          h.isString(O) ? O.trim() : O,
          _,
          g
        )) === !0 && m(A, _ ? _.concat(O) : [O]);
      }), u.pop();
    }
  }
  if (!h.isObject(e))
    throw new TypeError("data must be an object");
  return m(e), t;
}
function an(e) {
  const t = {
    "!": "%21",
    "'": "%27",
    "(": "%28",
    ")": "%29",
    "~": "%7E",
    "%20": "+",
    "%00": "\0"
  };
  return encodeURIComponent(e).replace(/[!'()~]|%20|%00/g, function(r) {
    return t[r];
  });
}
function Pt(e, t) {
  this._pairs = [], e && rt(e, this, t);
}
const Wn = Pt.prototype;
Wn.append = function(t, n) {
  this._pairs.push([t, n]);
};
Wn.toString = function(t) {
  const n = t ? function(r) {
    return t.call(this, r, an);
  } : an;
  return this._pairs.map(function(s) {
    return n(s[0]) + "=" + n(s[1]);
  }, "").join("&");
};
function io(e) {
  return encodeURIComponent(e).replace(/%3A/gi, ":").replace(/%24/g, "$").replace(/%2C/gi, ",").replace(/%20/g, "+").replace(/%5B/gi, "[").replace(/%5D/gi, "]");
}
function Hn(e, t, n) {
  if (!t)
    return e;
  const r = n && n.encode || io, s = n && n.serialize;
  let o;
  if (s ? o = s(t, n) : o = h.isURLSearchParams(t) ? t.toString() : new Pt(t, n).toString(r), o) {
    const i = e.indexOf("#");
    i !== -1 && (e = e.slice(0, i)), e += (e.indexOf("?") === -1 ? "?" : "&") + o;
  }
  return e;
}
class ln {
  constructor() {
    this.handlers = [];
  }
  /**
   * Add a new interceptor to the stack
   *
   * @param {Function} fulfilled The function to handle `then` for a `Promise`
   * @param {Function} rejected The function to handle `reject` for a `Promise`
   *
   * @return {Number} An ID used to remove interceptor later
   */
  use(t, n, r) {
    return this.handlers.push({
      fulfilled: t,
      rejected: n,
      synchronous: r ? r.synchronous : !1,
      runWhen: r ? r.runWhen : null
    }), this.handlers.length - 1;
  }
  /**
   * Remove an interceptor from the stack
   *
   * @param {Number} id The ID that was returned by `use`
   *
   * @returns {Boolean} `true` if the interceptor was removed, `false` otherwise
   */
  eject(t) {
    this.handlers[t] && (this.handlers[t] = null);
  }
  /**
   * Clear all interceptors from the stack
   *
   * @returns {void}
   */
  clear() {
    this.handlers && (this.handlers = []);
  }
  /**
   * Iterate over all the registered interceptors
   *
   * This method is particularly useful for skipping over any
   * interceptors that may have become `null` calling `eject`.
   *
   * @param {Function} fn The function to call for each interceptor
   *
   * @returns {void}
   */
  forEach(t) {
    h.forEach(this.handlers, function(r) {
      r !== null && t(r);
    });
  }
}
const zn = {
  silentJSONParsing: !0,
  forcedJSONParsing: !0,
  clarifyTimeoutError: !1
}, ao = typeof URLSearchParams < "u" ? URLSearchParams : Pt, lo = typeof FormData < "u" ? FormData : null, co = typeof Blob < "u" ? Blob : null, uo = {
  isBrowser: !0,
  classes: {
    URLSearchParams: ao,
    FormData: lo,
    Blob: co
  },
  protocols: ["http", "https", "file", "blob", "url", "data"]
}, Nt = typeof window < "u" && typeof document < "u", fo = ((e) => Nt && ["ReactNative", "NativeScript", "NS"].indexOf(e) < 0)(typeof navigator < "u" && navigator.product), ho = typeof WorkerGlobalScope < "u" && // eslint-disable-next-line no-undef
self instanceof WorkerGlobalScope && typeof self.importScripts == "function", po = Nt && window.location.href || "http://localhost", mo = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  hasBrowserEnv: Nt,
  hasStandardBrowserEnv: fo,
  hasStandardBrowserWebWorkerEnv: ho,
  origin: po
}, Symbol.toStringTag, { value: "Module" })), te = {
  ...mo,
  ...uo
};
function go(e, t) {
  return rt(e, new te.classes.URLSearchParams(), Object.assign({
    visitor: function(n, r, s, o) {
      return te.isNode && h.isBuffer(n) ? (this.append(r, n.toString("base64")), !1) : o.defaultVisitor.apply(this, arguments);
    }
  }, t));
}
function wo(e) {
  return h.matchAll(/\w+|\[(\w*)]/g, e).map((t) => t[0] === "[]" ? "" : t[1] || t[0]);
}
function yo(e) {
  const t = {}, n = Object.keys(e);
  let r;
  const s = n.length;
  let o;
  for (r = 0; r < s; r++)
    o = n[r], t[o] = e[o];
  return t;
}
function Kn(e) {
  function t(n, r, s, o) {
    let i = n[o++];
    if (i === "__proto__") return !0;
    const l = Number.isFinite(+i), c = o >= n.length;
    return i = !i && h.isArray(s) ? s.length : i, c ? (h.hasOwnProp(s, i) ? s[i] = [s[i], r] : s[i] = r, !l) : ((!s[i] || !h.isObject(s[i])) && (s[i] = []), t(n, r, s[i], o) && h.isArray(s[i]) && (s[i] = yo(s[i])), !l);
  }
  if (h.isFormData(e) && h.isFunction(e.entries)) {
    const n = {};
    return h.forEachEntry(e, (r, s) => {
      t(wo(r), s, n, 0);
    }), n;
  }
  return null;
}
function _o(e, t, n) {
  if (h.isString(e))
    try {
      return (t || JSON.parse)(e), h.trim(e);
    } catch (r) {
      if (r.name !== "SyntaxError")
        throw r;
    }
  return (n || JSON.stringify)(e);
}
const Ie = {
  transitional: zn,
  adapter: ["xhr", "http", "fetch"],
  transformRequest: [function(t, n) {
    const r = n.getContentType() || "", s = r.indexOf("application/json") > -1, o = h.isObject(t);
    if (o && h.isHTMLForm(t) && (t = new FormData(t)), h.isFormData(t))
      return s ? JSON.stringify(Kn(t)) : t;
    if (h.isArrayBuffer(t) || h.isBuffer(t) || h.isStream(t) || h.isFile(t) || h.isBlob(t) || h.isReadableStream(t))
      return t;
    if (h.isArrayBufferView(t))
      return t.buffer;
    if (h.isURLSearchParams(t))
      return n.setContentType("application/x-www-form-urlencoded;charset=utf-8", !1), t.toString();
    let l;
    if (o) {
      if (r.indexOf("application/x-www-form-urlencoded") > -1)
        return go(t, this.formSerializer).toString();
      if ((l = h.isFileList(t)) || r.indexOf("multipart/form-data") > -1) {
        const c = this.env && this.env.FormData;
        return rt(
          l ? { "files[]": t } : t,
          c && new c(),
          this.formSerializer
        );
      }
    }
    return o || s ? (n.setContentType("application/json", !1), _o(t)) : t;
  }],
  transformResponse: [function(t) {
    const n = this.transitional || Ie.transitional, r = n && n.forcedJSONParsing, s = this.responseType === "json";
    if (h.isResponse(t) || h.isReadableStream(t))
      return t;
    if (t && h.isString(t) && (r && !this.responseType || s)) {
      const i = !(n && n.silentJSONParsing) && s;
      try {
        return JSON.parse(t);
      } catch (l) {
        if (i)
          throw l.name === "SyntaxError" ? T.from(l, T.ERR_BAD_RESPONSE, this, null, this.response) : l;
      }
    }
    return t;
  }],
  /**
   * A timeout in milliseconds to abort a request. If set to 0 (default) a
   * timeout is not created.
   */
  timeout: 0,
  xsrfCookieName: "XSRF-TOKEN",
  xsrfHeaderName: "X-XSRF-TOKEN",
  maxContentLength: -1,
  maxBodyLength: -1,
  env: {
    FormData: te.classes.FormData,
    Blob: te.classes.Blob
  },
  validateStatus: function(t) {
    return t >= 200 && t < 300;
  },
  headers: {
    common: {
      Accept: "application/json, text/plain, */*",
      "Content-Type": void 0
    }
  }
};
h.forEach(["delete", "get", "head", "post", "put", "patch"], (e) => {
  Ie.headers[e] = {};
});
const bo = h.toObjectSet([
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
]), vo = (e) => {
  const t = {};
  let n, r, s;
  return e && e.split(`
`).forEach(function(i) {
    s = i.indexOf(":"), n = i.substring(0, s).trim().toLowerCase(), r = i.substring(s + 1).trim(), !(!n || t[n] && bo[n]) && (n === "set-cookie" ? t[n] ? t[n].push(r) : t[n] = [r] : t[n] = t[n] ? t[n] + ", " + r : r);
  }), t;
}, cn = Symbol("internals");
function Te(e) {
  return e && String(e).trim().toLowerCase();
}
function Ge(e) {
  return e === !1 || e == null ? e : h.isArray(e) ? e.map(Ge) : String(e);
}
function Eo(e) {
  const t = /* @__PURE__ */ Object.create(null), n = /([^\s,;=]+)\s*(?:=\s*([^,;]+))?/g;
  let r;
  for (; r = n.exec(e); )
    t[r[1]] = r[2];
  return t;
}
const Ro = (e) => /^[-_a-zA-Z0-9^`|~,!#$%&'*+.]+$/.test(e.trim());
function mt(e, t, n, r, s) {
  if (h.isFunction(r))
    return r.call(this, t, n);
  if (s && (t = n), !!h.isString(t)) {
    if (h.isString(r))
      return t.indexOf(r) !== -1;
    if (h.isRegExp(r))
      return r.test(t);
  }
}
function xo(e) {
  return e.trim().toLowerCase().replace(/([a-z\d])(\w*)/g, (t, n, r) => n.toUpperCase() + r);
}
function So(e, t) {
  const n = h.toCamelCase(" " + t);
  ["get", "set", "has"].forEach((r) => {
    Object.defineProperty(e, r + n, {
      value: function(s, o, i) {
        return this[r].call(this, t, s, o, i);
      },
      configurable: !0
    });
  });
}
class J {
  constructor(t) {
    t && this.set(t);
  }
  set(t, n, r) {
    const s = this;
    function o(l, c, d) {
      const a = Te(c);
      if (!a)
        throw new Error("header name must be a non-empty string");
      const u = h.findKey(s, a);
      (!u || s[u] === void 0 || d === !0 || d === void 0 && s[u] !== !1) && (s[u || c] = Ge(l));
    }
    const i = (l, c) => h.forEach(l, (d, a) => o(d, a, c));
    if (h.isPlainObject(t) || t instanceof this.constructor)
      i(t, n);
    else if (h.isString(t) && (t = t.trim()) && !Ro(t))
      i(vo(t), n);
    else if (h.isHeaders(t))
      for (const [l, c] of t.entries())
        o(c, l, r);
    else
      t != null && o(n, t, r);
    return this;
  }
  get(t, n) {
    if (t = Te(t), t) {
      const r = h.findKey(this, t);
      if (r) {
        const s = this[r];
        if (!n)
          return s;
        if (n === !0)
          return Eo(s);
        if (h.isFunction(n))
          return n.call(this, s, r);
        if (h.isRegExp(n))
          return n.exec(s);
        throw new TypeError("parser must be boolean|regexp|function");
      }
    }
  }
  has(t, n) {
    if (t = Te(t), t) {
      const r = h.findKey(this, t);
      return !!(r && this[r] !== void 0 && (!n || mt(this, this[r], r, n)));
    }
    return !1;
  }
  delete(t, n) {
    const r = this;
    let s = !1;
    function o(i) {
      if (i = Te(i), i) {
        const l = h.findKey(r, i);
        l && (!n || mt(r, r[l], l, n)) && (delete r[l], s = !0);
      }
    }
    return h.isArray(t) ? t.forEach(o) : o(t), s;
  }
  clear(t) {
    const n = Object.keys(this);
    let r = n.length, s = !1;
    for (; r--; ) {
      const o = n[r];
      (!t || mt(this, this[o], o, t, !0)) && (delete this[o], s = !0);
    }
    return s;
  }
  normalize(t) {
    const n = this, r = {};
    return h.forEach(this, (s, o) => {
      const i = h.findKey(r, o);
      if (i) {
        n[i] = Ge(s), delete n[o];
        return;
      }
      const l = t ? xo(o) : String(o).trim();
      l !== o && delete n[o], n[l] = Ge(s), r[l] = !0;
    }), this;
  }
  concat(...t) {
    return this.constructor.concat(this, ...t);
  }
  toJSON(t) {
    const n = /* @__PURE__ */ Object.create(null);
    return h.forEach(this, (r, s) => {
      r != null && r !== !1 && (n[s] = t && h.isArray(r) ? r.join(", ") : r);
    }), n;
  }
  [Symbol.iterator]() {
    return Object.entries(this.toJSON())[Symbol.iterator]();
  }
  toString() {
    return Object.entries(this.toJSON()).map(([t, n]) => t + ": " + n).join(`
`);
  }
  get [Symbol.toStringTag]() {
    return "AxiosHeaders";
  }
  static from(t) {
    return t instanceof this ? t : new this(t);
  }
  static concat(t, ...n) {
    const r = new this(t);
    return n.forEach((s) => r.set(s)), r;
  }
  static accessor(t) {
    const r = (this[cn] = this[cn] = {
      accessors: {}
    }).accessors, s = this.prototype;
    function o(i) {
      const l = Te(i);
      r[l] || (So(s, i), r[l] = !0);
    }
    return h.isArray(t) ? t.forEach(o) : o(t), this;
  }
}
J.accessor(["Content-Type", "Content-Length", "Accept", "Accept-Encoding", "User-Agent", "Authorization"]);
h.reduceDescriptors(J.prototype, ({ value: e }, t) => {
  let n = t[0].toUpperCase() + t.slice(1);
  return {
    get: () => e,
    set(r) {
      this[n] = r;
    }
  };
});
h.freezeMethods(J);
function gt(e, t) {
  const n = this || Ie, r = t || n, s = J.from(r.headers);
  let o = r.data;
  return h.forEach(e, function(l) {
    o = l.call(n, o, s.normalize(), t ? t.status : void 0);
  }), s.normalize(), o;
}
function Yn(e) {
  return !!(e && e.__CANCEL__);
}
function Oe(e, t, n) {
  T.call(this, e ?? "canceled", T.ERR_CANCELED, t, n), this.name = "CanceledError";
}
h.inherits(Oe, T, {
  __CANCEL__: !0
});
function Gn(e, t, n) {
  const r = n.config.validateStatus;
  !n.status || !r || r(n.status) ? e(n) : t(new T(
    "Request failed with status code " + n.status,
    [T.ERR_BAD_REQUEST, T.ERR_BAD_RESPONSE][Math.floor(n.status / 100) - 4],
    n.config,
    n.request,
    n
  ));
}
function Oo(e) {
  const t = /^([-+\w]{1,25})(:?\/\/|:)/.exec(e);
  return t && t[1] || "";
}
function qo(e, t) {
  e = e || 10;
  const n = new Array(e), r = new Array(e);
  let s = 0, o = 0, i;
  return t = t !== void 0 ? t : 1e3, function(c) {
    const d = Date.now(), a = r[o];
    i || (i = d), n[s] = c, r[s] = d;
    let u = o, g = 0;
    for (; u !== s; )
      g += n[u++], u = u % e;
    if (s = (s + 1) % e, s === o && (o = (o + 1) % e), d - i < t)
      return;
    const m = a && d - a;
    return m ? Math.round(g * 1e3 / m) : void 0;
  };
}
function Ao(e, t) {
  let n = 0;
  const r = 1e3 / t;
  let s = null;
  return function() {
    const i = this === !0, l = Date.now();
    if (i || l - n > r)
      return s && (clearTimeout(s), s = null), n = l, e.apply(null, arguments);
    s || (s = setTimeout(() => (s = null, n = Date.now(), e.apply(null, arguments)), r - (l - n)));
  };
}
const Je = (e, t, n = 3) => {
  let r = 0;
  const s = qo(50, 250);
  return Ao((o) => {
    const i = o.loaded, l = o.lengthComputable ? o.total : void 0, c = i - r, d = s(c), a = i <= l;
    r = i;
    const u = {
      loaded: i,
      total: l,
      progress: l ? i / l : void 0,
      bytes: c,
      rate: d || void 0,
      estimated: d && l && a ? (l - i) / d : void 0,
      event: o,
      lengthComputable: l != null
    };
    u[t ? "download" : "upload"] = !0, e(u);
  }, n);
}, To = te.hasStandardBrowserEnv ? (
  // Standard browser envs have full support of the APIs needed to test
  // whether the request URL is of the same origin as current location.
  function() {
    const t = /(msie|trident)/i.test(navigator.userAgent), n = document.createElement("a");
    let r;
    function s(o) {
      let i = o;
      return t && (n.setAttribute("href", i), i = n.href), n.setAttribute("href", i), {
        href: n.href,
        protocol: n.protocol ? n.protocol.replace(/:$/, "") : "",
        host: n.host,
        search: n.search ? n.search.replace(/^\?/, "") : "",
        hash: n.hash ? n.hash.replace(/^#/, "") : "",
        hostname: n.hostname,
        port: n.port,
        pathname: n.pathname.charAt(0) === "/" ? n.pathname : "/" + n.pathname
      };
    }
    return r = s(window.location.href), function(i) {
      const l = h.isString(i) ? s(i) : i;
      return l.protocol === r.protocol && l.host === r.host;
    };
  }()
) : (
  // Non standard browser envs (web workers, react-native) lack needed support.
  /* @__PURE__ */ function() {
    return function() {
      return !0;
    };
  }()
), ko = te.hasStandardBrowserEnv ? (
  // Standard browser envs support document.cookie
  {
    write(e, t, n, r, s, o) {
      const i = [e + "=" + encodeURIComponent(t)];
      h.isNumber(n) && i.push("expires=" + new Date(n).toGMTString()), h.isString(r) && i.push("path=" + r), h.isString(s) && i.push("domain=" + s), o === !0 && i.push("secure"), document.cookie = i.join("; ");
    },
    read(e) {
      const t = document.cookie.match(new RegExp("(^|;\\s*)(" + e + ")=([^;]*)"));
      return t ? decodeURIComponent(t[3]) : null;
    },
    remove(e) {
      this.write(e, "", Date.now() - 864e5);
    }
  }
) : (
  // Non-standard browser env (web workers, react-native) lack needed support.
  {
    write() {
    },
    read() {
      return null;
    },
    remove() {
    }
  }
);
function Co(e) {
  return /^([a-z][a-z\d+\-.]*:)?\/\//i.test(e);
}
function Po(e, t) {
  return t ? e.replace(/\/?\/$/, "") + "/" + t.replace(/^\/+/, "") : e;
}
function Jn(e, t) {
  return e && !Co(t) ? Po(e, t) : t;
}
const un = (e) => e instanceof J ? { ...e } : e;
function we(e, t) {
  t = t || {};
  const n = {};
  function r(d, a, u) {
    return h.isPlainObject(d) && h.isPlainObject(a) ? h.merge.call({ caseless: u }, d, a) : h.isPlainObject(a) ? h.merge({}, a) : h.isArray(a) ? a.slice() : a;
  }
  function s(d, a, u) {
    if (h.isUndefined(a)) {
      if (!h.isUndefined(d))
        return r(void 0, d, u);
    } else return r(d, a, u);
  }
  function o(d, a) {
    if (!h.isUndefined(a))
      return r(void 0, a);
  }
  function i(d, a) {
    if (h.isUndefined(a)) {
      if (!h.isUndefined(d))
        return r(void 0, d);
    } else return r(void 0, a);
  }
  function l(d, a, u) {
    if (u in t)
      return r(d, a);
    if (u in e)
      return r(void 0, d);
  }
  const c = {
    url: o,
    method: o,
    data: o,
    baseURL: i,
    transformRequest: i,
    transformResponse: i,
    paramsSerializer: i,
    timeout: i,
    timeoutMessage: i,
    withCredentials: i,
    withXSRFToken: i,
    adapter: i,
    responseType: i,
    xsrfCookieName: i,
    xsrfHeaderName: i,
    onUploadProgress: i,
    onDownloadProgress: i,
    decompress: i,
    maxContentLength: i,
    maxBodyLength: i,
    beforeRedirect: i,
    transport: i,
    httpAgent: i,
    httpsAgent: i,
    cancelToken: i,
    socketPath: i,
    responseEncoding: i,
    validateStatus: l,
    headers: (d, a) => s(un(d), un(a), !0)
  };
  return h.forEach(Object.keys(Object.assign({}, e, t)), function(a) {
    const u = c[a] || s, g = u(e[a], t[a], a);
    h.isUndefined(g) && u !== l || (n[a] = g);
  }), n;
}
const Qn = (e) => {
  const t = we({}, e);
  let { data: n, withXSRFToken: r, xsrfHeaderName: s, xsrfCookieName: o, headers: i, auth: l } = t;
  t.headers = i = J.from(i), t.url = Hn(Jn(t.baseURL, t.url), e.params, e.paramsSerializer), l && i.set(
    "Authorization",
    "Basic " + btoa((l.username || "") + ":" + (l.password ? unescape(encodeURIComponent(l.password)) : ""))
  );
  let c;
  if (h.isFormData(n)) {
    if (te.hasStandardBrowserEnv || te.hasStandardBrowserWebWorkerEnv)
      i.setContentType(void 0);
    else if ((c = i.getContentType()) !== !1) {
      const [d, ...a] = c ? c.split(";").map((u) => u.trim()).filter(Boolean) : [];
      i.setContentType([d || "multipart/form-data", ...a].join("; "));
    }
  }
  if (te.hasStandardBrowserEnv && (r && h.isFunction(r) && (r = r(t)), r || r !== !1 && To(t.url))) {
    const d = s && o && ko.read(o);
    d && i.set(s, d);
  }
  return t;
}, No = typeof XMLHttpRequest < "u", Vo = No && function(e) {
  return new Promise(function(n, r) {
    const s = Qn(e);
    let o = s.data;
    const i = J.from(s.headers).normalize();
    let { responseType: l } = s, c;
    function d() {
      s.cancelToken && s.cancelToken.unsubscribe(c), s.signal && s.signal.removeEventListener("abort", c);
    }
    let a = new XMLHttpRequest();
    a.open(s.method.toUpperCase(), s.url, !0), a.timeout = s.timeout;
    function u() {
      if (!a)
        return;
      const m = J.from(
        "getAllResponseHeaders" in a && a.getAllResponseHeaders()
      ), _ = {
        data: !l || l === "text" || l === "json" ? a.responseText : a.response,
        status: a.status,
        statusText: a.statusText,
        headers: m,
        config: e,
        request: a
      };
      Gn(function(A) {
        n(A), d();
      }, function(A) {
        r(A), d();
      }, _), a = null;
    }
    "onloadend" in a ? a.onloadend = u : a.onreadystatechange = function() {
      !a || a.readyState !== 4 || a.status === 0 && !(a.responseURL && a.responseURL.indexOf("file:") === 0) || setTimeout(u);
    }, a.onabort = function() {
      a && (r(new T("Request aborted", T.ECONNABORTED, s, a)), a = null);
    }, a.onerror = function() {
      r(new T("Network Error", T.ERR_NETWORK, s, a)), a = null;
    }, a.ontimeout = function() {
      let w = s.timeout ? "timeout of " + s.timeout + "ms exceeded" : "timeout exceeded";
      const _ = s.transitional || zn;
      s.timeoutErrorMessage && (w = s.timeoutErrorMessage), r(new T(
        w,
        _.clarifyTimeoutError ? T.ETIMEDOUT : T.ECONNABORTED,
        s,
        a
      )), a = null;
    }, o === void 0 && i.setContentType(null), "setRequestHeader" in a && h.forEach(i.toJSON(), function(w, _) {
      a.setRequestHeader(_, w);
    }), h.isUndefined(s.withCredentials) || (a.withCredentials = !!s.withCredentials), l && l !== "json" && (a.responseType = s.responseType), typeof s.onDownloadProgress == "function" && a.addEventListener("progress", Je(s.onDownloadProgress, !0)), typeof s.onUploadProgress == "function" && a.upload && a.upload.addEventListener("progress", Je(s.onUploadProgress)), (s.cancelToken || s.signal) && (c = (m) => {
      a && (r(!m || m.type ? new Oe(null, e, a) : m), a.abort(), a = null);
    }, s.cancelToken && s.cancelToken.subscribe(c), s.signal && (s.signal.aborted ? c() : s.signal.addEventListener("abort", c)));
    const g = Oo(s.url);
    if (g && te.protocols.indexOf(g) === -1) {
      r(new T("Unsupported protocol " + g + ":", T.ERR_BAD_REQUEST, e));
      return;
    }
    a.send(o || null);
  });
}, Lo = (e, t) => {
  let n = new AbortController(), r;
  const s = function(c) {
    if (!r) {
      r = !0, i();
      const d = c instanceof Error ? c : this.reason;
      n.abort(d instanceof T ? d : new Oe(d instanceof Error ? d.message : d));
    }
  };
  let o = t && setTimeout(() => {
    s(new T(`timeout ${t} of ms exceeded`, T.ETIMEDOUT));
  }, t);
  const i = () => {
    e && (o && clearTimeout(o), o = null, e.forEach((c) => {
      c && (c.removeEventListener ? c.removeEventListener("abort", s) : c.unsubscribe(s));
    }), e = null);
  };
  e.forEach((c) => c && c.addEventListener && c.addEventListener("abort", s));
  const { signal: l } = n;
  return l.unsubscribe = i, [l, () => {
    o && clearTimeout(o), o = null;
  }];
}, Fo = function* (e, t) {
  let n = e.byteLength;
  if (!t || n < t) {
    yield e;
    return;
  }
  let r = 0, s;
  for (; r < n; )
    s = r + t, yield e.slice(r, s), r = s;
}, Uo = async function* (e, t, n) {
  for await (const r of e)
    yield* Fo(ArrayBuffer.isView(r) ? r : await n(String(r)), t);
}, dn = (e, t, n, r, s) => {
  const o = Uo(e, t, s);
  let i = 0;
  return new ReadableStream({
    type: "bytes",
    async pull(l) {
      const { done: c, value: d } = await o.next();
      if (c) {
        l.close(), r();
        return;
      }
      let a = d.byteLength;
      n && n(i += a), l.enqueue(new Uint8Array(d));
    },
    cancel(l) {
      return r(l), o.return();
    }
  }, {
    highWaterMark: 2
  });
}, fn = (e, t) => {
  const n = e != null;
  return (r) => setTimeout(() => t({
    lengthComputable: n,
    total: e,
    loaded: r
  }));
}, st = typeof fetch == "function" && typeof Request == "function" && typeof Response == "function", Zn = st && typeof ReadableStream == "function", xt = st && (typeof TextEncoder == "function" ? /* @__PURE__ */ ((e) => (t) => e.encode(t))(new TextEncoder()) : async (e) => new Uint8Array(await new Response(e).arrayBuffer())), Bo = Zn && (() => {
  let e = !1;
  const t = new Request(te.origin, {
    body: new ReadableStream(),
    method: "POST",
    get duplex() {
      return e = !0, "half";
    }
  }).headers.has("Content-Type");
  return e && !t;
})(), hn = 64 * 1024, St = Zn && !!(() => {
  try {
    return h.isReadableStream(new Response("").body);
  } catch {
  }
})(), Qe = {
  stream: St && ((e) => e.body)
};
st && ((e) => {
  ["text", "arrayBuffer", "blob", "formData", "stream"].forEach((t) => {
    !Qe[t] && (Qe[t] = h.isFunction(e[t]) ? (n) => n[t]() : (n, r) => {
      throw new T(`Response type '${t}' is not supported`, T.ERR_NOT_SUPPORT, r);
    });
  });
})(new Response());
const Do = async (e) => {
  if (e == null)
    return 0;
  if (h.isBlob(e))
    return e.size;
  if (h.isSpecCompliantForm(e))
    return (await new Request(e).arrayBuffer()).byteLength;
  if (h.isArrayBufferView(e))
    return e.byteLength;
  if (h.isURLSearchParams(e) && (e = e + ""), h.isString(e))
    return (await xt(e)).byteLength;
}, Io = async (e, t) => {
  const n = h.toFiniteNumber(e.getContentLength());
  return n ?? Do(t);
}, Mo = st && (async (e) => {
  let {
    url: t,
    method: n,
    data: r,
    signal: s,
    cancelToken: o,
    timeout: i,
    onDownloadProgress: l,
    onUploadProgress: c,
    responseType: d,
    headers: a,
    withCredentials: u = "same-origin",
    fetchOptions: g
  } = Qn(e);
  d = d ? (d + "").toLowerCase() : "text";
  let [m, w] = s || o || i ? Lo([s, o], i) : [], _, q;
  const A = () => {
    !_ && setTimeout(() => {
      m && m.unsubscribe();
    }), _ = !0;
  };
  let O;
  try {
    if (c && Bo && n !== "get" && n !== "head" && (O = await Io(a, r)) !== 0) {
      let $ = new Request(t, {
        method: "POST",
        body: r,
        duplex: "half"
      }), D;
      h.isFormData(r) && (D = $.headers.get("content-type")) && a.setContentType(D), $.body && (r = dn($.body, hn, fn(
        O,
        Je(c)
      ), null, xt));
    }
    h.isString(u) || (u = u ? "cors" : "omit"), q = new Request(t, {
      ...g,
      signal: m,
      method: n.toUpperCase(),
      headers: a.normalize().toJSON(),
      body: r,
      duplex: "half",
      withCredentials: u
    });
    let E = await fetch(q);
    const M = St && (d === "stream" || d === "response");
    if (St && (l || M)) {
      const $ = {};
      ["status", "statusText", "headers"].forEach((ye) => {
        $[ye] = E[ye];
      });
      const D = h.toFiniteNumber(E.headers.get("content-length"));
      E = new Response(
        dn(E.body, hn, l && fn(
          D,
          Je(l, !0)
        ), M && A, xt),
        $
      );
    }
    d = d || "text";
    let Y = await Qe[h.findKey(Qe, d) || "text"](E, e);
    return !M && A(), w && w(), await new Promise(($, D) => {
      Gn($, D, {
        data: Y,
        headers: J.from(E.headers),
        status: E.status,
        statusText: E.statusText,
        config: e,
        request: q
      });
    });
  } catch (E) {
    throw A(), E && E.name === "TypeError" && /fetch/i.test(E.message) ? Object.assign(
      new T("Network Error", T.ERR_NETWORK, e, q),
      {
        cause: E.cause || E
      }
    ) : T.from(E, E && E.code, e, q);
  }
}), Ot = {
  http: ro,
  xhr: Vo,
  fetch: Mo
};
h.forEach(Ot, (e, t) => {
  if (e) {
    try {
      Object.defineProperty(e, "name", { value: t });
    } catch {
    }
    Object.defineProperty(e, "adapterName", { value: t });
  }
});
const pn = (e) => `- ${e}`, $o = (e) => h.isFunction(e) || e === null || e === !1, Xn = {
  getAdapter: (e) => {
    e = h.isArray(e) ? e : [e];
    const { length: t } = e;
    let n, r;
    const s = {};
    for (let o = 0; o < t; o++) {
      n = e[o];
      let i;
      if (r = n, !$o(n) && (r = Ot[(i = String(n)).toLowerCase()], r === void 0))
        throw new T(`Unknown adapter '${i}'`);
      if (r)
        break;
      s[i || "#" + o] = r;
    }
    if (!r) {
      const o = Object.entries(s).map(
        ([l, c]) => `adapter ${l} ` + (c === !1 ? "is not supported by the environment" : "is not available in the build")
      );
      let i = t ? o.length > 1 ? `since :
` + o.map(pn).join(`
`) : " " + pn(o[0]) : "as no adapter specified";
      throw new T(
        "There is no suitable adapter to dispatch the request " + i,
        "ERR_NOT_SUPPORT"
      );
    }
    return r;
  },
  adapters: Ot
};
function wt(e) {
  if (e.cancelToken && e.cancelToken.throwIfRequested(), e.signal && e.signal.aborted)
    throw new Oe(null, e);
}
function mn(e) {
  return wt(e), e.headers = J.from(e.headers), e.data = gt.call(
    e,
    e.transformRequest
  ), ["post", "put", "patch"].indexOf(e.method) !== -1 && e.headers.setContentType("application/x-www-form-urlencoded", !1), Xn.getAdapter(e.adapter || Ie.adapter)(e).then(function(r) {
    return wt(e), r.data = gt.call(
      e,
      e.transformResponse,
      r
    ), r.headers = J.from(r.headers), r;
  }, function(r) {
    return Yn(r) || (wt(e), r && r.response && (r.response.data = gt.call(
      e,
      e.transformResponse,
      r.response
    ), r.response.headers = J.from(r.response.headers))), Promise.reject(r);
  });
}
const er = "1.7.2", Vt = {};
["object", "boolean", "number", "function", "string", "symbol"].forEach((e, t) => {
  Vt[e] = function(r) {
    return typeof r === e || "a" + (t < 1 ? "n " : " ") + e;
  };
});
const gn = {};
Vt.transitional = function(t, n, r) {
  function s(o, i) {
    return "[Axios v" + er + "] Transitional option '" + o + "'" + i + (r ? ". " + r : "");
  }
  return (o, i, l) => {
    if (t === !1)
      throw new T(
        s(i, " has been removed" + (n ? " in " + n : "")),
        T.ERR_DEPRECATED
      );
    return n && !gn[i] && (gn[i] = !0, console.warn(
      s(
        i,
        " has been deprecated since v" + n + " and will be removed in the near future"
      )
    )), t ? t(o, i, l) : !0;
  };
};
function jo(e, t, n) {
  if (typeof e != "object")
    throw new T("options must be an object", T.ERR_BAD_OPTION_VALUE);
  const r = Object.keys(e);
  let s = r.length;
  for (; s-- > 0; ) {
    const o = r[s], i = t[o];
    if (i) {
      const l = e[o], c = l === void 0 || i(l, o, e);
      if (c !== !0)
        throw new T("option " + o + " must be " + c, T.ERR_BAD_OPTION_VALUE);
      continue;
    }
    if (n !== !0)
      throw new T("Unknown option " + o, T.ERR_BAD_OPTION);
  }
}
const qt = {
  assertOptions: jo,
  validators: Vt
}, de = qt.validators;
class ge {
  constructor(t) {
    this.defaults = t, this.interceptors = {
      request: new ln(),
      response: new ln()
    };
  }
  /**
   * Dispatch a request
   *
   * @param {String|Object} configOrUrl The config specific for this request (merged with this.defaults)
   * @param {?Object} config
   *
   * @returns {Promise} The Promise to be fulfilled
   */
  async request(t, n) {
    try {
      return await this._request(t, n);
    } catch (r) {
      if (r instanceof Error) {
        let s;
        Error.captureStackTrace ? Error.captureStackTrace(s = {}) : s = new Error();
        const o = s.stack ? s.stack.replace(/^.+\n/, "") : "";
        try {
          r.stack ? o && !String(r.stack).endsWith(o.replace(/^.+\n.+\n/, "")) && (r.stack += `
` + o) : r.stack = o;
        } catch {
        }
      }
      throw r;
    }
  }
  _request(t, n) {
    typeof t == "string" ? (n = n || {}, n.url = t) : n = t || {}, n = we(this.defaults, n);
    const { transitional: r, paramsSerializer: s, headers: o } = n;
    r !== void 0 && qt.assertOptions(r, {
      silentJSONParsing: de.transitional(de.boolean),
      forcedJSONParsing: de.transitional(de.boolean),
      clarifyTimeoutError: de.transitional(de.boolean)
    }, !1), s != null && (h.isFunction(s) ? n.paramsSerializer = {
      serialize: s
    } : qt.assertOptions(s, {
      encode: de.function,
      serialize: de.function
    }, !0)), n.method = (n.method || this.defaults.method || "get").toLowerCase();
    let i = o && h.merge(
      o.common,
      o[n.method]
    );
    o && h.forEach(
      ["delete", "get", "head", "post", "put", "patch", "common"],
      (w) => {
        delete o[w];
      }
    ), n.headers = J.concat(i, o);
    const l = [];
    let c = !0;
    this.interceptors.request.forEach(function(_) {
      typeof _.runWhen == "function" && _.runWhen(n) === !1 || (c = c && _.synchronous, l.unshift(_.fulfilled, _.rejected));
    });
    const d = [];
    this.interceptors.response.forEach(function(_) {
      d.push(_.fulfilled, _.rejected);
    });
    let a, u = 0, g;
    if (!c) {
      const w = [mn.bind(this), void 0];
      for (w.unshift.apply(w, l), w.push.apply(w, d), g = w.length, a = Promise.resolve(n); u < g; )
        a = a.then(w[u++], w[u++]);
      return a;
    }
    g = l.length;
    let m = n;
    for (u = 0; u < g; ) {
      const w = l[u++], _ = l[u++];
      try {
        m = w(m);
      } catch (q) {
        _.call(this, q);
        break;
      }
    }
    try {
      a = mn.call(this, m);
    } catch (w) {
      return Promise.reject(w);
    }
    for (u = 0, g = d.length; u < g; )
      a = a.then(d[u++], d[u++]);
    return a;
  }
  getUri(t) {
    t = we(this.defaults, t);
    const n = Jn(t.baseURL, t.url);
    return Hn(n, t.params, t.paramsSerializer);
  }
}
h.forEach(["delete", "get", "head", "options"], function(t) {
  ge.prototype[t] = function(n, r) {
    return this.request(we(r || {}, {
      method: t,
      url: n,
      data: (r || {}).data
    }));
  };
});
h.forEach(["post", "put", "patch"], function(t) {
  function n(r) {
    return function(o, i, l) {
      return this.request(we(l || {}, {
        method: t,
        headers: r ? {
          "Content-Type": "multipart/form-data"
        } : {},
        url: o,
        data: i
      }));
    };
  }
  ge.prototype[t] = n(), ge.prototype[t + "Form"] = n(!0);
});
class Lt {
  constructor(t) {
    if (typeof t != "function")
      throw new TypeError("executor must be a function.");
    let n;
    this.promise = new Promise(function(o) {
      n = o;
    });
    const r = this;
    this.promise.then((s) => {
      if (!r._listeners) return;
      let o = r._listeners.length;
      for (; o-- > 0; )
        r._listeners[o](s);
      r._listeners = null;
    }), this.promise.then = (s) => {
      let o;
      const i = new Promise((l) => {
        r.subscribe(l), o = l;
      }).then(s);
      return i.cancel = function() {
        r.unsubscribe(o);
      }, i;
    }, t(function(o, i, l) {
      r.reason || (r.reason = new Oe(o, i, l), n(r.reason));
    });
  }
  /**
   * Throws a `CanceledError` if cancellation has been requested.
   */
  throwIfRequested() {
    if (this.reason)
      throw this.reason;
  }
  /**
   * Subscribe to the cancel signal
   */
  subscribe(t) {
    if (this.reason) {
      t(this.reason);
      return;
    }
    this._listeners ? this._listeners.push(t) : this._listeners = [t];
  }
  /**
   * Unsubscribe from the cancel signal
   */
  unsubscribe(t) {
    if (!this._listeners)
      return;
    const n = this._listeners.indexOf(t);
    n !== -1 && this._listeners.splice(n, 1);
  }
  /**
   * Returns an object that contains a new `CancelToken` and a function that, when called,
   * cancels the `CancelToken`.
   */
  static source() {
    let t;
    return {
      token: new Lt(function(s) {
        t = s;
      }),
      cancel: t
    };
  }
}
function Wo(e) {
  return function(n) {
    return e.apply(null, n);
  };
}
function Ho(e) {
  return h.isObject(e) && e.isAxiosError === !0;
}
const At = {
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
  NetworkAuthenticationRequired: 511
};
Object.entries(At).forEach(([e, t]) => {
  At[t] = e;
});
function tr(e) {
  const t = new ge(e), n = Nn(ge.prototype.request, t);
  return h.extend(n, ge.prototype, t, { allOwnKeys: !0 }), h.extend(n, t, null, { allOwnKeys: !0 }), n.create = function(s) {
    return tr(we(e, s));
  }, n;
}
const U = tr(Ie);
U.Axios = ge;
U.CanceledError = Oe;
U.CancelToken = Lt;
U.isCancel = Yn;
U.VERSION = er;
U.toFormData = rt;
U.AxiosError = T;
U.Cancel = U.CanceledError;
U.all = function(t) {
  return Promise.all(t);
};
U.spread = Wo;
U.isAxiosError = Ho;
U.mergeConfig = we;
U.AxiosHeaders = J;
U.formToJSON = (e) => Kn(h.isHTMLForm(e) ? new FormData(e) : e);
U.getAdapter = Xn.getAdapter;
U.HttpStatusCode = At;
U.default = U;
const Me = (e, t) => {
  const n = e.__vccOpts || e;
  for (const [r, s] of t)
    n[r] = s;
  return n;
}, fe = window.Quasar, zo = {
  created() {
    this.getWeek(0);
  },
  methods: {
    formatDate(e) {
      let t = fe.date.extractDate(
        e.substring(0, 16),
        "YYYY-MM-DDTHH:mm"
      );
      return fe.date.formatDate(t, "DD/MM/YYYY");
    },
    updateData() {
      this.loading = !0, this.fail = !1, this.data = [], U.get(
        `${this.apiUrl}/executions/summaries?weekOffset=${this.offset}&status=${this.status}`
      ).then((e) => {
        e.data.map((t) => {
          t.lastExecutionTime = t.lastExecutionTime ? this.formatDate(t.lastExecutionTime) : "-", t.nextExecutionTime = t.nextExecutionTime ? this.formatDate(t.nextExecutionTime) : "-", this.data.push(t);
        }), this.loading = !1;
      }).catch((e) => {
        this.fail = !0, this.loading = !1, console.error(e);
      });
    },
    getWeekLimits: function(e) {
      let t = fe.date.addToDate(Date.now(), {
        days: e * 7
      }), n = fe.date.getDayOfWeek(Date.now()), r = fe.date.subtractFromDate(t, {
        days: n ? n - 1 : 6
      }), s = fe.date.addToDate(t, {
        days: n ? 7 - n : 0
      });
      return {
        startOfWeek: fe.date.formatDate(r, "DD/MM/YYYY"),
        endOfWeek: fe.date.formatDate(s, "DD/MM/YYYY")
      };
    },
    getWeek(e) {
      this.offset = e ? this.offset + e : 0;
      let t = this.getWeekLimits(this.offset);
      this.startOfWeek = t.startOfWeek, this.endOfWeek = t.endOfWeek, this.status || (this.status = "A"), this.updateData();
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
          label: this.$q.lang.vuiOrchestra.orchestra.processLabel,
          align: "left",
          field: "processLabel"
        },
        {
          name: "state",
          label: this.$q.lang.vuiOrchestra.orchestra.state,
          field: "state",
          align: "center"
        },
        {
          name: "lastExecutionTime",
          label: this.$q.lang.vuiOrchestra.orchestra.lastExecutionTime,
          field: "lastExecutionTime"
        },
        {
          name: "nextExecutionTime",
          label: this.$q.lang.vuiOrchestra.orchestra.nextExecutionTime,
          field: "nextExecutionTime"
        }
      ],
      data: [],
      loading: !1,
      fail: !1,
      offset: 0,
      status: "A",
      //Status must not be empty nor in lowercase letters. Any uppercase string would do the job as long as it is different than the keywords: SUCCESS, ERROR et MISFIRED
      tab: "all",
      startOfWeek: "",
      endOfWeek: ""
    };
  },
  watch: {
    "$q.lang": function() {
      this.columns = this.columns.map((e) => ({ ...e, label: this.$q.lang.vuiOrchestra.orchestra[e.name] }));
    }
  }
}, Q = window.Vue.toDisplayString, K = window.Vue.createElementVNode, he = window.Vue.resolveComponent, z = window.Vue.createVNode, pe = window.Vue.withCtx, He = window.Vue.createTextVNode, ze = window.Vue.openBlock, Ke = window.Vue.createElementBlock;
window.Vue.createCommentVNode;
const Ko = { class: "row justify-center" }, Yo = { style: { width: "1300px" } }, Go = { class: "text-h5 row q-mt-lg" }, Jo = { class: "q-mx-auto" }, Qo = { class: "row q-mt-lg" }, Zo = { class: "row q-mt-lg" }, Xo = { class: "q-mx-auto q-gutter-md" }, ei = { class: "q-pa-md" }, ti = { class: "row q-col-gutter-x-md" }, ni = { class: "col text-center" }, ri = { class: "col text-center" }, si = { class: "col text-center" }, oi = { class: "row q-col-gutter-x-md" }, ii = { class: "col text-center" }, ai = { class: "col text-center" }, li = { class: "col text-center" }, ci = { class: "full-width row flex-center q-gutter-sm" }, ui = { key: 0 }, di = { key: 1 }, fi = { key: 2 };
function hi(e, t, n, r, s, o) {
  const i = he("q-tab"), l = he("q-tabs"), c = he("q-btn"), d = he("q-td"), a = he("q-icon"), u = he("q-tr"), g = he("q-spinner"), m = he("q-table");
  return ze(), Ke("div", Ko, [
    K("div", Yo, [
      K("div", Go, [
        K("div", Jo, Q(e.$q.lang.vuiOrchestra.orchestra.title.part1) + " " + Q(s.startOfWeek) + " " + Q(e.$q.lang.vuiOrchestra.orchestra.title.part2) + " " + Q(s.endOfWeek), 1)
      ]),
      K("div", Qo, [
        z(l, {
          modelValue: s.tab,
          "onUpdate:modelValue": t[4] || (t[4] = (w) => s.tab = w),
          "inline-label": "",
          class: "text-primary q-mx-auto"
        }, {
          default: pe(() => [
            z(i, {
              name: "all",
              icon: "list",
              label: e.$q.lang.vuiOrchestra.orchestra.all,
              onClick: t[0] || (t[0] = (w) => o.getStatus("A"))
            }, null, 8, ["label"]),
            z(i, {
              name: "success",
              icon: "done",
              label: e.$q.lang.vuiOrchestra.orchestra.success,
              onClick: t[1] || (t[1] = (w) => o.getStatus("SUCCESS")),
              class: "text-green"
            }, null, 8, ["label"]),
            z(i, {
              name: "error",
              icon: "error",
              label: e.$q.lang.vuiOrchestra.orchestra.error,
              onClick: t[2] || (t[2] = (w) => o.getStatus("ERROR")),
              class: "text-red"
            }, null, 8, ["label"]),
            z(i, {
              name: "misfired",
              icon: "timer_off",
              label: e.$q.lang.vuiOrchestra.orchestra.misfired,
              onClick: t[3] || (t[3] = (w) => o.getStatus("MISFIRED")),
              class: "text-grey"
            }, null, 8, ["label"])
          ]),
          _: 1
        }, 8, ["modelValue"])
      ]),
      K("div", Zo, [
        K("div", Xo, [
          z(c, {
            color: "primary",
            icon: "navigate_before",
            label: e.$q.lang.vuiOrchestra.orchestra.previousWeek,
            onClick: t[5] || (t[5] = (w) => o.getWeek(-1))
          }, null, 8, ["label"]),
          z(c, {
            round: "",
            color: "primary",
            icon: "today",
            onClick: t[6] || (t[6] = (w) => o.getWeek(0))
          }),
          z(c, {
            color: "primary",
            "icon-right": "navigate_next",
            label: e.$q.lang.vuiOrchestra.orchestra.nextWeek,
            onClick: t[7] || (t[7] = (w) => o.getWeek(1))
          }, null, 8, ["label"])
        ])
      ]),
      K("div", ei, [
        z(m, {
          rows: s.data,
          columns: s.columns,
          "row-key": "name",
          loading: s.loading
        }, {
          body: pe((w) => [
            z(u, {
              props: w,
              onClick: (_) => e.$router.push("/process/" + w.row.processName)
            }, {
              default: pe(() => [
                z(d, {
                  key: "processLabel",
                  props: w
                }, {
                  default: pe(() => [
                    He(Q(w.row.processLabel), 1)
                  ]),
                  _: 2
                }, 1032, ["props"]),
                z(d, {
                  key: "state",
                  props: w
                }, {
                  default: pe(() => [
                    K("div", ti, [
                      K("div", ni, [
                        z(a, {
                          name: "done",
                          size: "sm",
                          class: "text-green"
                        })
                      ]),
                      K("div", ri, [
                        z(a, {
                          name: "error",
                          size: "sm",
                          class: "text-red"
                        })
                      ]),
                      K("div", si, [
                        z(a, {
                          name: "timer_off",
                          size: "sm",
                          class: "text-grey"
                        })
                      ])
                    ]),
                    K("div", oi, [
                      K("div", ii, Q(w.row.successfulCount), 1),
                      K("div", ai, Q(w.row.errorsCount), 1),
                      K("div", li, Q(w.row.misfiredCount), 1)
                    ])
                  ]),
                  _: 2
                }, 1032, ["props"]),
                z(d, {
                  key: "lastExecutionTime",
                  props: w
                }, {
                  default: pe(() => [
                    He(Q(w.row.lastExecutionTime), 1)
                  ]),
                  _: 2
                }, 1032, ["props"]),
                z(d, {
                  key: "nextExecutionTime",
                  props: w
                }, {
                  default: pe(() => [
                    He(Q(w.row.nextExecutionTime), 1)
                  ]),
                  _: 2
                }, 1032, ["props"])
              ]),
              _: 2
            }, 1032, ["props", "onClick"])
          ]),
          "no-data": pe(() => [
            K("div", ci, [
              s.loading ? (ze(), Ke("span", ui, [
                He(Q(e.$q.lang.vuiOrchestra.orchestra.loading) + " ", 1),
                z(g)
              ])) : s.fail ? (ze(), Ke("span", di, Q(e.$q.lang.vuiOrchestra.orchestra.connectionFailed), 1)) : (ze(), Ke("span", fi, Q(e.$q.lang.vuiOrchestra.orchestra.noData), 1))
            ])
          ]),
          _: 1
        }, 8, ["rows", "columns", "loading"])
      ])
    ])
  ]);
}
const nr = /* @__PURE__ */ Me(zo, [["render", hi]]), wn = window.Quasar, pi = {
  created() {
    U.get(`${this.apiUrl}/definitions/${this.$props.processName}`).then((e) => {
      this.processInfo = e.data, this.form.technical = {
        cronExpression: this.processInfo.triggeringStrategy ? this.processInfo.triggeringStrategy.cronExpression : "",
        active: Object.entries(this.processInfo).length ? this.processInfo.active ? "true" : "false" : "-",
        multiExecution: this.processInfo.triggeringStrategy ? this.processInfo.triggeringStrategy.multiExecution ? "true" : "false" : "-",
        rescuePeriod: this.processInfo.triggeringStrategy ? this.processInfo.triggeringStrategy.rescuePeriodInSeconds : ""
      }, this.form.settings = this.processInfo.triggeringStrategy ? this.processInfo.triggeringStrategy.initialParams ? { ...this.processInfo.triggeringStrategy.initialParams } : {} : {};
    }).catch((e) => {
      console.error(e), e.response ? this.errorMessage = `${this.$q.lang.vuiOrchestra.orchestra.noProcess}: ${this.$props.processName}` : this.errorMessage = this.$q.lang.vuiOrchestra.orchestra.connectionFailed, this.connectionFailure = !e.response;
    }), U.get(`${this.apiUrl}/executions/summaries/${this.$props.processName}`).then((e) => {
      this.processSummary = e.data;
    }).catch((e) => {
      console.error(e);
    });
  },
  props: ["apiUrl", "orchestraUiReadOnly", "processName"],
  data() {
    return {
      editMode: { technical: !1, settings: !1 },
      executionsLoaded: !1,
      limit: 20,
      offset: 0,
      connectionFailure: "?",
      errorMessage: "",
      status: "",
      processInfo: {},
      processSummary: {},
      filterTab: "all",
      // Tab for filtering executions
      executions: [],
      activities: {},
      expandedExecutions: {},
      form: {
        technical: {},
        settings: {}
      },
      tabs: {},
      //Tabs for navigating inside execution
      splitterModel: 50,
      splitterModelExecutions: 20
    };
  },
  watch: {
    "$q.lang": function() {
      this.errorMessage && (this.connectionFailure == !1 ? this.errorMessage = `${this.$q.lang.vuiOrchestra.orchestra.noProcess}: ${this.$props.processName}` : this.connectionFailure == !0 && (this.errorMessage = this.$q.lang.vuiOrchestra.orchestra.connectionFailed));
    }
  },
  methods: {
    getIconFromExecutionState(e) {
      switch (e) {
        case "DONE":
          return "done";
        case "ERROR":
          return "error";
        case "ABORTED":
          return "flash_on";
        default:
          return "help";
      }
    },
    getColorFromExecutionState(e) {
      switch (e) {
        case "DONE":
          return "green";
        case "ERROR":
          return "red";
        case "ABORTED":
          return "orange";
        default:
          return "grey";
      }
    },
    formatDate(e) {
      if (e) {
        let t = wn.date.extractDate(
          e.substring(0, 16),
          "YYYY-MM-DDTHH:mm"
        );
        return wn.date.formatDate(t, "DD/MM/YYYY HH:mm");
      }
      return "";
    },
    executeNow: function(e) {
      U.post(`${this.apiUrl}/executionsControl/executeNow`, { processName: e }).then((t) => {
        this.$q.notify({
          message: this.$q.lang.vuiOrchestra.orchestra.executeNowOk,
          color: "positive"
        });
      }).catch((t) => {
        console.error(t), this.$q.notify({
          message: this.$q.lang.vuiOrchestra.orchestra.executeNowErr,
          color: "negative"
        });
      });
    },
    fetchActivities: function(e) {
      U.get(`${this.apiUrl}/executions/${e}/activities`).then((t) => {
        this.activities[e] = t.data;
      }).catch((t) => {
        console.error(t);
      });
    },
    updateExecutions: function(e) {
      this.limit = 20, this.offset = 0, this.status = e, this.errorMessage = "", this.connectionFailure = "?", U.get(
        `${this.apiUrl}/executions/?processName=${this.$props.processName}&status=${this.status}&limit=${this.limit}`
      ).then((t) => {
        this.executions = t.data, t.data.map((n) => {
          this.tabs[n.preId] = "info";
        });
      }).catch((t) => {
        console.error(t), t.response ? this.errorMessage = `${this.$q.lang.vuiOrchestra.orchestra.noProcess}: ${this.$props.processName}` : this.errorMessage = this.$q.lang.vuiOrchestra.orchestra.connectionFailed, this.connectionFailure = !t.response;
      });
    },
    onLoad(e, t) {
      U.get(
        `${this.apiUrl}/executions/?processName=${this.$props.processName}&status=${this.status}&limit=${this.limit}&offset=${this.offset}`
      ).then((n) => {
        this.executions.push(...n.data), n.data.map((r) => {
          this.tabs[r.preId] = "info";
        }), n.data.length < this.limit && (this.executionsLoaded = !0, t(!0)), t(!1);
      }), this.offset += 20;
    },
    onSubmit(e) {
      e == "technical" ? U.put(
        `${this.apiUrl}/definitions/${this.$props.processName}/properties`,
        {
          cronExpression: this.form.technical.cronExpression,
          multiExecution: this.form.technical.multiExecution == "true",
          rescuePeriod: this.form.technical.rescuePeriod,
          active: this.form.technical.active == "true"
        }
      ).then(this.$router.go()).catch(console.error) : e == "settings" && U.put(`${this.apiUrl}/definitions/${this.$props.processName}/params`, {
        initialParams: this.form.settings
      }).then(this.$router.go()).catch(console.error);
    }
  }
}, R = window.Vue.toDisplayString, F = window.Vue.openBlock, H = window.Vue.createElementBlock, oe = window.Vue.createCommentVNode, I = window.Vue.resolveComponent, f = window.Vue.createVNode, V = window.Vue.createElementVNode, p = window.Vue.withCtx, C = window.Vue.createTextVNode, le = window.Vue.createBlock, ke = window.Vue.renderList, Ce = window.Vue.Fragment, mi = { class: "q-pa-md text-primary" }, gi = { class: "text-h4 q-mb-md" }, wi = { key: 0 }, yi = { key: 1 }, _i = { class: "text-h6" }, bi = { class: "col" }, vi = { class: "text-weight-medium" }, Ei = { class: "col" }, Ri = { class: "text-weight-medium" }, xi = { class: "col" }, Si = { class: "text-weight-medium" }, Oi = { class: "text-h6" }, qi = { class: "text-h6" }, Ai = { key: 0 }, Ti = { class: "q-gutter-sm" }, ki = { class: "q-gutter-sm" }, Ci = { key: 1 }, Pi = { class: "text-h6" }, Ni = { key: 0 }, Vi = { key: 0 }, Li = { key: 1 }, Fi = { class: "q-pa-md" }, Ui = { class: "text-h4 q-gutter-x-md q-mx-auto row" }, Bi = { class: "col" }, Di = { class: "col-auto" }, Ii = { class: "text-h5" }, Mi = {
  style: { "max-width": "400px" },
  class: "text-primary"
}, $i = { class: "text-weight-medium" }, ji = { class: "text-weight-medium" }, Wi = { class: "text-weight-medium" }, Hi = { class: "text-h5" }, zi = { style: { "max-width": "400px" } }, Ki = { class: "text-weight-medium" }, Yi = { class: "text-weight-medium" }, Gi = { class: "text-weight-medium" }, Ji = { class: "text-weight-medium" }, Qi = { class: "text-weight-medium" }, Zi = { class: "row justify-center q-my-md" }, Xi = { key: 0 }, ea = {
  key: 1,
  class: "text-weight-medium"
};
function ta(e, t, n, r, s, o) {
  const i = I("q-spinner-ios"), l = I("q-card-section"), c = I("q-separator"), d = I("q-icon"), a = I("q-item-section"), u = I("q-item"), g = I("q-list"), m = I("q-btn"), w = I("q-card"), _ = I("q-input"), q = I("q-radio"), A = I("q-form"), O = I("q-tab"), E = I("q-tabs"), M = I("q-avatar"), Y = I("q-tooltip"), $ = I("q-tab-panel"), D = I("q-expansion-item"), ye = I("q-tab-panels"), _e = I("q-splitter"), $e = I("q-infinite-scroll");
  return F(), le(_e, {
    modelValue: s.splitterModel,
    "onUpdate:modelValue": t[16] || (t[16] = (S) => s.splitterModel = S),
    style: { height: "85vh" },
    limits: [0, 100]
  }, {
    before: p(() => [
      V("div", mi, [
        V("div", gi, [
          s.processInfo.label || s.errorMessage ? (F(), H("div", wi, R(s.processInfo.label || s.errorMessage), 1)) : (F(), H("div", yi, [
            f(i, {
              color: "primary",
              size: "sm"
            })
          ]))
        ]),
        f(w, { class: "my-card" }, {
          default: p(() => [
            f(l, null, {
              default: p(() => [
                V("div", _i, R(e.$q.lang.vuiOrchestra.orchestra.totalExecutions), 1)
              ]),
              _: 1
            }),
            f(c, { inset: "" }),
            f(l, { class: "row" }, {
              default: p(() => [
                V("div", bi, [
                  f(g, null, {
                    default: p(() => [
                      f(u, null, {
                        default: p(() => [
                          f(a, null, {
                            default: p(() => [
                              V("div", vi, [
                                f(d, {
                                  color: "green",
                                  name: "done",
                                  size: "sm"
                                }),
                                C(" " + R(s.processSummary.successfulCount), 1)
                              ])
                            ]),
                            _: 1
                          })
                        ]),
                        _: 1
                      })
                    ]),
                    _: 1
                  })
                ]),
                V("div", Ei, [
                  f(g, null, {
                    default: p(() => [
                      f(u, null, {
                        default: p(() => [
                          f(a, null, {
                            default: p(() => [
                              V("div", Ri, [
                                f(d, {
                                  color: "red",
                                  name: "error",
                                  size: "sm"
                                }),
                                C(" " + R(s.processSummary.errorsCount), 1)
                              ])
                            ]),
                            _: 1
                          })
                        ]),
                        _: 1
                      })
                    ]),
                    _: 1
                  })
                ]),
                V("div", xi, [
                  f(g, null, {
                    default: p(() => [
                      f(u, null, {
                        default: p(() => [
                          f(a, null, {
                            default: p(() => [
                              V("div", Si, [
                                f(d, {
                                  color: "grey",
                                  name: "timer_off",
                                  size: "sm"
                                }),
                                C(" " + R(s.processSummary.misfiredCount), 1)
                              ])
                            ]),
                            _: 1
                          })
                        ]),
                        _: 1
                      })
                    ]),
                    _: 1
                  })
                ])
              ]),
              _: 1
            }),
            f(c, { inset: "" }),
            n.orchestraUiReadOnly ? oe("", !0) : (F(), le(l, { key: 0 }, {
              default: p(() => [
                f(m, {
                  label: e.$q.lang.vuiOrchestra.orchestra.executeNow,
                  onClick: t[0] || (t[0] = (S) => o.executeNow(s.processInfo.definitionId)),
                  color: "indigo",
                  class: "q-mx-auto"
                }, null, 8, ["label"])
              ]),
              _: 1
            }))
          ]),
          _: 1
        }),
        s.processInfo.metadatas && Object.keys(s.processInfo.metadatas).length > 0 ? (F(), le(w, {
          key: 0,
          class: "my-card q-mt-lg text-primary"
        }, {
          default: p(() => [
            f(l, null, {
              default: p(() => [
                V("div", Oi, R(e.$q.lang.vuiOrchestra.orchestra.functionalId), 1)
              ]),
              _: 1
            }),
            f(c, { inset: "" }),
            f(l, null, {
              default: p(() => [
                f(g, { dense: "" }, {
                  default: p(() => [
                    (F(!0), H(Ce, null, ke(s.processInfo.metadatas, (S, P) => (F(), H("div", { key: P }, [
                      f(u, null, {
                        default: p(() => [
                          f(a, { class: "text-weight-medium" }, {
                            default: p(() => [
                              C(R(P), 1)
                            ]),
                            _: 2
                          }, 1024),
                          f(a, { side: "" }, {
                            default: p(() => [
                              C(R(S), 1)
                            ]),
                            _: 2
                          }, 1024)
                        ]),
                        _: 2
                      }, 1024)
                    ]))), 128))
                  ]),
                  _: 1
                })
              ]),
              _: 1
            })
          ]),
          _: 1
        })) : oe("", !0),
        f(w, { class: "my-card q-mt-lg text-primary" }, {
          default: p(() => [
            f(l, null, {
              default: p(() => [
                V("div", qi, [
                  C(R(e.$q.lang.vuiOrchestra.orchestra.technicalId) + " ", 1),
                  n.orchestraUiReadOnly ? oe("", !0) : (F(), le(m, {
                    key: 0,
                    round: "",
                    color: "indigo",
                    icon: "edit",
                    class: "q-ml-sm",
                    size: "sm",
                    onClick: t[1] || (t[1] = (S) => s.editMode.technical = !s.editMode.technical)
                  }))
                ])
              ]),
              _: 1
            }),
            f(c, { inset: "" }),
            f(l, { class: "q-gutter-sm" }, {
              default: p(() => [
                V("div", null, [
                  f(g, { dense: "" }, {
                    default: p(() => [
                      s.editMode.technical ? (F(), H("div", Ai, [
                        f(A, {
                          onSubmit: t[8] || (t[8] = (S) => o.onSubmit("technical"))
                        }, {
                          default: p(() => [
                            f(u, null, {
                              default: p(() => [
                                f(a, { class: "text-weight-medium" }, {
                                  default: p(() => [
                                    C(R(e.$q.lang.vuiOrchestra.orchestra.processName), 1)
                                  ]),
                                  _: 1
                                }),
                                f(a, { side: "" }, {
                                  default: p(() => [
                                    C(R(s.processInfo.definitionId), 1)
                                  ]),
                                  _: 1
                                })
                              ]),
                              _: 1
                            }),
                            f(u, null, {
                              default: p(() => [
                                f(a, { class: "text-weight-medium" }, {
                                  default: p(() => [
                                    C(R(e.$q.lang.vuiOrchestra.orchestra.cronExpression), 1)
                                  ]),
                                  _: 1
                                }),
                                f(a, { side: "" }, {
                                  default: p(() => [
                                    f(_, {
                                      modelValue: s.form.technical.cronExpression,
                                      "onUpdate:modelValue": t[2] || (t[2] = (S) => s.form.technical.cronExpression = S),
                                      dense: !0,
                                      placeholder: "* * * * * ? *"
                                    }, null, 8, ["modelValue"])
                                  ]),
                                  _: 1
                                })
                              ]),
                              _: 1
                            }),
                            f(u, null, {
                              default: p(() => [
                                f(a, { class: "text-weight-medium" }, {
                                  default: p(() => [
                                    C(R(e.$q.lang.vuiOrchestra.orchestra.active), 1)
                                  ]),
                                  _: 1
                                }),
                                f(a, { side: "" }, {
                                  default: p(() => [
                                    V("div", Ti, [
                                      f(q, {
                                        size: "xs",
                                        modelValue: s.form.technical.active,
                                        "onUpdate:modelValue": t[3] || (t[3] = (S) => s.form.technical.active = S),
                                        val: "true",
                                        label: e.$q.lang.vuiOrchestra.orchestra.yes
                                      }, null, 8, ["modelValue", "label"]),
                                      f(q, {
                                        size: "xs",
                                        modelValue: s.form.technical.active,
                                        "onUpdate:modelValue": t[4] || (t[4] = (S) => s.form.technical.active = S),
                                        val: "false",
                                        label: e.$q.lang.vuiOrchestra.orchestra.no
                                      }, null, 8, ["modelValue", "label"])
                                    ])
                                  ]),
                                  _: 1
                                })
                              ]),
                              _: 1
                            }),
                            f(u, null, {
                              default: p(() => [
                                f(a, { class: "text-weight-medium" }, {
                                  default: p(() => [
                                    C(R(e.$q.lang.vuiOrchestra.orchestra.multiExecution), 1)
                                  ]),
                                  _: 1
                                }),
                                f(a, { side: "" }, {
                                  default: p(() => [
                                    V("div", ki, [
                                      f(q, {
                                        size: "xs",
                                        modelValue: s.form.technical.multiExecution,
                                        "onUpdate:modelValue": t[5] || (t[5] = (S) => s.form.technical.multiExecution = S),
                                        val: "true",
                                        label: e.$q.lang.vuiOrchestra.orchestra.yes
                                      }, null, 8, ["modelValue", "label"]),
                                      f(q, {
                                        size: "xs",
                                        modelValue: s.form.technical.multiExecution,
                                        "onUpdate:modelValue": t[6] || (t[6] = (S) => s.form.technical.multiExecution = S),
                                        val: "false",
                                        label: e.$q.lang.vuiOrchestra.orchestra.no
                                      }, null, 8, ["modelValue", "label"])
                                    ])
                                  ]),
                                  _: 1
                                })
                              ]),
                              _: 1
                            }),
                            f(u, null, {
                              default: p(() => [
                                f(a, { class: "text-weight-medium" }, {
                                  default: p(() => [
                                    C(R(e.$q.lang.vuiOrchestra.orchestra.rescuePeriod), 1)
                                  ]),
                                  _: 1
                                }),
                                f(a, { side: "" }, {
                                  default: p(() => [
                                    f(_, {
                                      modelValue: s.form.technical.rescuePeriod,
                                      "onUpdate:modelValue": t[7] || (t[7] = (S) => s.form.technical.rescuePeriod = S),
                                      modelModifiers: { number: !0 },
                                      type: "number",
                                      dense: !0
                                    }, null, 8, ["modelValue"])
                                  ]),
                                  _: 1
                                })
                              ]),
                              _: 1
                            }),
                            V("div", null, [
                              f(u, null, {
                                default: p(() => [
                                  n.orchestraUiReadOnly ? oe("", !0) : (F(), le(m, {
                                    key: 0,
                                    label: e.$q.lang.vuiOrchestra.orchestra.submit,
                                    type: "submit",
                                    color: "indigo",
                                    class: "q-mx-auto"
                                  }, null, 8, ["label"]))
                                ]),
                                _: 1
                              })
                            ])
                          ]),
                          _: 1
                        })
                      ])) : (F(), H("div", Ci, [
                        f(u, null, {
                          default: p(() => [
                            f(a, { class: "text-weight-medium" }, {
                              default: p(() => [
                                C(R(e.$q.lang.vuiOrchestra.orchestra.processName), 1)
                              ]),
                              _: 1
                            }),
                            f(a, { side: "" }, {
                              default: p(() => [
                                C(R(s.processInfo.definitionId), 1)
                              ]),
                              _: 1
                            })
                          ]),
                          _: 1
                        }),
                        f(u, null, {
                          default: p(() => [
                            f(a, { class: "text-weight-medium" }, {
                              default: p(() => [
                                C(R(e.$q.lang.vuiOrchestra.orchestra.cronExpression), 1)
                              ]),
                              _: 1
                            }),
                            f(a, { side: "" }, {
                              default: p(() => [
                                C(R(s.processInfo.triggeringStrategy ? s.processInfo.triggeringStrategy.cronExpression : "-"), 1)
                              ]),
                              _: 1
                            })
                          ]),
                          _: 1
                        }),
                        f(u, null, {
                          default: p(() => [
                            f(a, { class: "text-weight-medium" }, {
                              default: p(() => [
                                C(R(e.$q.lang.vuiOrchestra.orchestra.active), 1)
                              ]),
                              _: 1
                            }),
                            f(a, { side: "" }, {
                              default: p(() => [
                                C(R(Object.entries(s.processInfo).length ? s.processInfo.active ? e.$q.lang.vuiOrchestra.orchestra.yes : e.$q.lang.vuiOrchestra.orchestra.no : "-"), 1)
                              ]),
                              _: 1
                            })
                          ]),
                          _: 1
                        }),
                        f(u, null, {
                          default: p(() => [
                            f(a, { class: "text-weight-medium" }, {
                              default: p(() => [
                                C(R(e.$q.lang.vuiOrchestra.orchestra.multiExecution), 1)
                              ]),
                              _: 1
                            }),
                            f(a, { side: "" }, {
                              default: p(() => [
                                C(R(s.processInfo.triggeringStrategy ? s.processInfo.triggeringStrategy.multiExecution ? e.$q.lang.vuiOrchestra.orchestra.yes : e.$q.lang.vuiOrchestra.orchestra.no : "-"), 1)
                              ]),
                              _: 1
                            })
                          ]),
                          _: 1
                        }),
                        f(u, null, {
                          default: p(() => [
                            f(a, { class: "text-weight-medium" }, {
                              default: p(() => [
                                C(R(e.$q.lang.vuiOrchestra.orchestra.rescuePeriod), 1)
                              ]),
                              _: 1
                            }),
                            f(a, { side: "" }, {
                              default: p(() => [
                                C(R(s.processInfo.triggeringStrategy ? s.processInfo.triggeringStrategy.rescuePeriodInSeconds : "-"), 1)
                              ]),
                              _: 1
                            })
                          ]),
                          _: 1
                        })
                      ]))
                    ]),
                    _: 1
                  })
                ])
              ]),
              _: 1
            })
          ]),
          _: 1
        }),
        f(w, { class: "my-card q-mt-lg text-primary" }, {
          default: p(() => [
            f(l, null, {
              default: p(() => [
                V("div", Pi, [
                  C(R(e.$q.lang.vuiOrchestra.orchestra.settings) + " ", 1),
                  n.orchestraUiReadOnly ? oe("", !0) : (F(), le(m, {
                    key: 0,
                    round: "",
                    color: "indigo",
                    icon: "edit",
                    class: "q-ml-sm",
                    size: "sm",
                    onClick: t[9] || (t[9] = (S) => s.editMode.settings = !s.editMode.settings)
                  }))
                ])
              ]),
              _: 1
            }),
            f(c, { inset: "" }),
            f(l, null, {
              default: p(() => [
                f(g, { dense: "" }, {
                  default: p(() => [
                    s.processInfo.triggeringStrategy && s.processInfo.triggeringStrategy.initialParams ? (F(), H("div", Ni, [
                      s.editMode.settings ? (F(), H("div", Vi, [
                        f(A, {
                          onSubmit: t[10] || (t[10] = (S) => o.onSubmit("settings"))
                        }, {
                          default: p(() => [
                            (F(!0), H(Ce, null, ke(s.processInfo.triggeringStrategy.initialParams, (S, P) => (F(), H("div", { key: P }, [
                              f(u, null, {
                                default: p(() => [
                                  f(a, { class: "text-weight-medium" }, {
                                    default: p(() => [
                                      C(R(P), 1)
                                    ]),
                                    _: 2
                                  }, 1024),
                                  f(a, { side: "" }, {
                                    default: p(() => [
                                      f(_, {
                                        modelValue: s.form.settings[P],
                                        "onUpdate:modelValue": (se) => s.form.settings[P] = se,
                                        dense: !0,
                                        rules: [
                                          (se) => se && se.length > 0 || e.$q.lang.vuiOrchestra.orchestra.fieldCannotBeEmpty
                                        ]
                                      }, null, 8, ["modelValue", "onUpdate:modelValue", "rules"])
                                    ]),
                                    _: 2
                                  }, 1024)
                                ]),
                                _: 2
                              }, 1024)
                            ]))), 128)),
                            V("div", null, [
                              f(u, null, {
                                default: p(() => [
                                  n.orchestraUiReadOnly ? oe("", !0) : (F(), le(m, {
                                    key: 0,
                                    label: e.$q.lang.vuiOrchestra.orchestra.submit,
                                    type: "submit",
                                    color: "indigo",
                                    class: "q-mx-auto"
                                  }, null, 8, ["label"]))
                                ]),
                                _: 1
                              })
                            ])
                          ]),
                          _: 1
                        })
                      ])) : (F(), H("div", Li, [
                        (F(!0), H(Ce, null, ke(s.processInfo.triggeringStrategy.initialParams, (S, P) => (F(), H("div", { key: P }, [
                          f(u, null, {
                            default: p(() => [
                              f(a, { class: "text-weight-medium" }, {
                                default: p(() => [
                                  C(R(P), 1)
                                ]),
                                _: 2
                              }, 1024),
                              f(a, { side: "" }, {
                                default: p(() => [
                                  C(R(S), 1)
                                ]),
                                _: 2
                              }, 1024)
                            ]),
                            _: 2
                          }, 1024)
                        ]))), 128))
                      ]))
                    ])) : oe("", !0)
                  ]),
                  _: 1
                })
              ]),
              _: 1
            })
          ]),
          _: 1
        })
      ])
    ]),
    after: p(() => [
      V("div", Fi, [
        V("div", Ui, [
          V("div", Bi, R(e.$q.lang.vuiOrchestra.orchestra.executions), 1),
          V("div", Di, [
            f(E, {
              modelValue: s.filterTab,
              "onUpdate:modelValue": t[14] || (t[14] = (S) => s.filterTab = S),
              "inline-label": "",
              class: "text-primary q-mx-auto"
            }, {
              default: p(() => [
                f(O, {
                  name: "all",
                  icon: "list",
                  label: e.$q.lang.vuiOrchestra.orchestra.all,
                  onClick: t[11] || (t[11] = (S) => o.updateExecutions(""))
                }, null, 8, ["label"]),
                f(O, {
                  name: "done",
                  icon: "done",
                  label: e.$q.lang.vuiOrchestra.orchestra.done,
                  onClick: t[12] || (t[12] = (S) => o.updateExecutions("DONE")),
                  class: "text-green"
                }, null, 8, ["label"]),
                f(O, {
                  name: "error",
                  icon: "error",
                  label: e.$q.lang.vuiOrchestra.orchestra.error,
                  onClick: t[13] || (t[13] = (S) => o.updateExecutions("ERROR")),
                  class: "text-red"
                }, null, 8, ["label"])
              ]),
              _: 1
            }, 8, ["modelValue"])
          ])
        ]),
        f($e, {
          onLoad: o.onLoad,
          offset: 50,
          "scroll-target": "div.q-splitter__panel.q-splitter__after.col"
        }, {
          loading: p(() => [
            V("div", Zi, [
              !s.errorMessage && !s.executionsLoaded ? (F(), H("div", Xi, [
                f(i, {
                  color: "primary",
                  size: "2em"
                })
              ])) : (F(), H("div", ea, R(s.errorMessage), 1))
            ])
          ]),
          default: p(() => [
            f(g, {
              bordered: "",
              class: "rounded-borders q-mt-sm"
            }, {
              default: p(() => [
                (F(!0), H(Ce, null, ke(s.executions, (S) => (F(), H("div", {
                  key: S.preId
                }, [
                  f(D, {
                    "expand-separator": "",
                    onShow: (P) => o.fetchActivities(S.preId)
                  }, {
                    header: p(() => [
                      f(a, { avatar: "" }, {
                        default: p(() => [
                          f(M, {
                            icon: o.getIconFromExecutionState(S.status),
                            color: o.getColorFromExecutionState(S.status),
                            "text-color": "white"
                          }, null, 8, ["icon", "color"]),
                          f(Y, null, {
                            default: p(() => [
                              C(R(S.status), 1)
                            ]),
                            _: 2
                          }, 1024)
                        ]),
                        _: 2
                      }, 1024),
                      f(a, { class: "text-weight-medium" }, {
                        default: p(() => [
                          C(R(o.formatDate(S.beginTime)) + " ", 1),
                          f(Y, {
                            delay: 1e3,
                            anchor: "top middle"
                          }, {
                            default: p(() => [
                              C(" preId: " + R(S.preId), 1)
                            ]),
                            _: 2
                          }, 1024)
                        ]),
                        _: 2
                      }, 1024)
                    ]),
                    default: p(() => [
                      f(w, null, {
                        default: p(() => [
                          f(c),
                          f(l, null, {
                            default: p(() => [
                              f(_e, {
                                modelValue: s.splitterModelExecutions,
                                "onUpdate:modelValue": t[15] || (t[15] = (P) => s.splitterModelExecutions = P)
                              }, {
                                before: p(() => [
                                  f(E, {
                                    modelValue: s.tabs[S.preId],
                                    "onUpdate:modelValue": (P) => s.tabs[S.preId] = P,
                                    vertical: "",
                                    class: "text-primary"
                                  }, {
                                    default: p(() => [
                                      f(O, {
                                        name: "info",
                                        label: e.$q.lang.vuiOrchestra.orchestra.informations
                                      }, null, 8, ["label"]),
                                      f(O, {
                                        name: "activities",
                                        label: e.$q.lang.vuiOrchestra.orchestra.activities
                                      }, null, 8, ["label"]),
                                      f(O, {
                                        name: "support",
                                        label: e.$q.lang.vuiOrchestra.orchestra.support
                                      }, null, 8, ["label"])
                                    ]),
                                    _: 2
                                  }, 1032, ["modelValue", "onUpdate:modelValue"])
                                ]),
                                after: p(() => [
                                  f(ye, {
                                    modelValue: s.tabs[S.preId],
                                    "onUpdate:modelValue": (P) => s.tabs[S.preId] = P,
                                    animated: "",
                                    vertical: "",
                                    "transition-prev": "jump-up",
                                    "transition-next": "jump-up"
                                  }, {
                                    default: p(() => [
                                      f($, { name: "info" }, {
                                        default: p(() => [
                                          V("div", Ii, R(e.$q.lang.vuiOrchestra.orchestra.informations), 1),
                                          f(c, { class: "q-mt-sm q-mb-md" }),
                                          V("div", Mi, [
                                            f(g, { dense: "" }, {
                                              default: p(() => [
                                                f(u, null, {
                                                  default: p(() => [
                                                    f(a, null, {
                                                      default: p(() => [
                                                        V("div", $i, R(e.$q.lang.vuiOrchestra.orchestra.startTime), 1)
                                                      ]),
                                                      _: 1
                                                    }),
                                                    f(a, {
                                                      side: "",
                                                      class: "text-primary"
                                                    }, {
                                                      default: p(() => [
                                                        C(R(o.formatDate(S.beginTime)), 1)
                                                      ]),
                                                      _: 2
                                                    }, 1024)
                                                  ]),
                                                  _: 2
                                                }, 1024),
                                                f(u, null, {
                                                  default: p(() => [
                                                    f(a, null, {
                                                      default: p(() => [
                                                        V("div", ji, R(e.$q.lang.vuiOrchestra.orchestra.endTime), 1)
                                                      ]),
                                                      _: 1
                                                    }),
                                                    f(a, {
                                                      side: "",
                                                      class: "text-primary"
                                                    }, {
                                                      default: p(() => [
                                                        C(R(o.formatDate(S.endTime)), 1)
                                                      ]),
                                                      _: 2
                                                    }, 1024)
                                                  ]),
                                                  _: 2
                                                }, 1024),
                                                f(u, null, {
                                                  default: p(() => [
                                                    f(a, null, {
                                                      default: p(() => [
                                                        V("div", Wi, R(e.$q.lang.vuiOrchestra.orchestra.duration), 1)
                                                      ]),
                                                      _: 1
                                                    }),
                                                    f(a, {
                                                      side: "",
                                                      class: "text-primary"
                                                    }, {
                                                      default: p(() => [
                                                        C(R(S.executionTime) + "s ", 1)
                                                      ]),
                                                      _: 2
                                                    }, 1024)
                                                  ]),
                                                  _: 2
                                                }, 1024)
                                              ]),
                                              _: 2
                                            }, 1024)
                                          ])
                                        ]),
                                        _: 2
                                      }, 1024),
                                      f($, { name: "activities" }, {
                                        default: p(() => [
                                          V("div", Hi, R(e.$q.lang.vuiOrchestra.orchestra.activities), 1),
                                          f(c, { class: "q-mt-sm q-mb-md" }),
                                          f(g, {
                                            bordered: "",
                                            class: "rounded-borders"
                                          }, {
                                            default: p(() => [
                                              (F(!0), H(Ce, null, ke(s.activities[S.preId], (P) => (F(), H("div", {
                                                key: P.aceId
                                              }, [
                                                f(D, { "expand-separator": "" }, {
                                                  header: p(() => [
                                                    f(a, { avatar: "" }, {
                                                      default: p(() => [
                                                        f(M, {
                                                          icon: o.getIconFromExecutionState(P.status),
                                                          color: o.getColorFromExecutionState(P.status),
                                                          "text-color": "white"
                                                        }, null, 8, ["icon", "color"]),
                                                        f(Y, null, {
                                                          default: p(() => [
                                                            C(R(P.status), 1)
                                                          ]),
                                                          _: 2
                                                        }, 1024)
                                                      ]),
                                                      _: 2
                                                    }, 1024),
                                                    f(a, { class: "text-weight-medium" }, {
                                                      default: p(() => [
                                                        C(R(P.label) + " ", 1),
                                                        f(Y, {
                                                          delay: 1e3,
                                                          anchor: "top middle"
                                                        }, {
                                                          default: p(() => [
                                                            C(" aceId: " + R(P.aceId), 1)
                                                          ]),
                                                          _: 2
                                                        }, 1024)
                                                      ]),
                                                      _: 2
                                                    }, 1024)
                                                  ]),
                                                  default: p(() => [
                                                    f(w, null, {
                                                      default: p(() => [
                                                        f(c),
                                                        f(l, null, {
                                                          default: p(() => [
                                                            V("div", zi, [
                                                              f(g, { dense: "" }, {
                                                                default: p(() => [
                                                                  f(u, null, {
                                                                    default: p(() => [
                                                                      f(a, null, {
                                                                        default: p(() => [
                                                                          V("div", Ki, R(e.$q.lang.vuiOrchestra.orchestra.startTime), 1)
                                                                        ]),
                                                                        _: 1
                                                                      }),
                                                                      f(a, {
                                                                        side: "",
                                                                        class: "text-primary"
                                                                      }, {
                                                                        default: p(() => [
                                                                          C(R(o.formatDate(P.beginTime)), 1)
                                                                        ]),
                                                                        _: 2
                                                                      }, 1024)
                                                                    ]),
                                                                    _: 2
                                                                  }, 1024),
                                                                  f(u, null, {
                                                                    default: p(() => [
                                                                      f(a, null, {
                                                                        default: p(() => [
                                                                          V("div", Yi, R(e.$q.lang.vuiOrchestra.orchestra.endTime), 1)
                                                                        ]),
                                                                        _: 1
                                                                      }),
                                                                      f(a, {
                                                                        side: "",
                                                                        class: "text-primary"
                                                                      }, {
                                                                        default: p(() => [
                                                                          C(R(o.formatDate(P.endTime)), 1)
                                                                        ]),
                                                                        _: 2
                                                                      }, 1024)
                                                                    ]),
                                                                    _: 2
                                                                  }, 1024),
                                                                  f(u, null, {
                                                                    default: p(() => [
                                                                      f(a, null, {
                                                                        default: p(() => [
                                                                          V("div", Gi, R(e.$q.lang.vuiOrchestra.orchestra.duration), 1)
                                                                        ]),
                                                                        _: 1
                                                                      }),
                                                                      f(a, {
                                                                        side: "",
                                                                        class: "text-primary"
                                                                      }, {
                                                                        default: p(() => [
                                                                          C(R(P.executionTime) + "s ", 1)
                                                                        ]),
                                                                        _: 2
                                                                      }, 1024)
                                                                    ]),
                                                                    _: 2
                                                                  }, 1024),
                                                                  P.hasLogFile ? (F(), le(u, { key: 0 }, {
                                                                    default: p(() => [
                                                                      f(a, null, {
                                                                        default: p(() => [
                                                                          V("div", Ji, R(e.$q.lang.vuiOrchestra.orchestra.logFile), 1)
                                                                        ]),
                                                                        _: 1
                                                                      }),
                                                                      f(a, {
                                                                        side: "",
                                                                        class: "text-primary"
                                                                      }, {
                                                                        default: p(() => [
                                                                          f(m, {
                                                                            type: "a",
                                                                            icon: "description",
                                                                            flat: "",
                                                                            padding: "none",
                                                                            title: e.$q.lang.vuiOrchestra.orchestra.logFile,
                                                                            href: n.apiUrl + "/executions/" + S.preId + "/activities/" + P.aceId + "/attachment"
                                                                          }, null, 8, ["title", "href"])
                                                                        ]),
                                                                        _: 2
                                                                      }, 1024)
                                                                    ]),
                                                                    _: 2
                                                                  }, 1024)) : oe("", !0),
                                                                  P.hasTechnicalLog ? (F(), le(u, { key: 1 }, {
                                                                    default: p(() => [
                                                                      f(a, null, {
                                                                        default: p(() => [
                                                                          V("div", Qi, R(e.$q.lang.vuiOrchestra.orchestra.technicalLogFile), 1)
                                                                        ]),
                                                                        _: 1
                                                                      }),
                                                                      f(a, {
                                                                        side: "",
                                                                        class: "text-primary"
                                                                      }, {
                                                                        default: p(() => [
                                                                          f(m, {
                                                                            type: "a",
                                                                            icon: "description",
                                                                            flat: "",
                                                                            padding: "none",
                                                                            title: e.$q.lang.vuiOrchestra.orchestra.technicalLogFile,
                                                                            href: n.apiUrl + "/executions/" + S.preId + "/activities/" + P.aceId + "/logFile"
                                                                          }, null, 8, ["title", "href"])
                                                                        ]),
                                                                        _: 2
                                                                      }, 1024)
                                                                    ]),
                                                                    _: 2
                                                                  }, 1024)) : oe("", !0)
                                                                ]),
                                                                _: 2
                                                              }, 1024)
                                                            ])
                                                          ]),
                                                          _: 2
                                                        }, 1024)
                                                      ]),
                                                      _: 2
                                                    }, 1024)
                                                  ]),
                                                  _: 2
                                                }, 1024),
                                                f(c)
                                              ]))), 128))
                                            ]),
                                            _: 2
                                          }, 1024)
                                        ]),
                                        _: 2
                                      }, 1024),
                                      oe("", !0)
                                    ]),
                                    _: 2
                                  }, 1032, ["modelValue", "onUpdate:modelValue"])
                                ]),
                                _: 2
                              }, 1032, ["modelValue"])
                            ]),
                            _: 2
                          }, 1024)
                        ]),
                        _: 2
                      }, 1024)
                    ]),
                    _: 2
                  }, 1032, ["onShow"]),
                  f(c)
                ]))), 128))
              ]),
              _: 1
            })
          ]),
          _: 1
        }, 8, ["onLoad"])
      ])
    ]),
    _: 1
  }, 8, ["modelValue"]);
}
const rr = /* @__PURE__ */ Me(pi, [["render", ta]]), sr = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEYAAABGCAYAAABxLuKEAAALlklEQVR4nOxc6W9c5dU/d7+zefaxPXa8O4kJcTDgkDcvLwECL4bAGxISB6K3pAWpVdU/gA8JEFBaAqWhUYC2iCKVIlREi1pRqaWiRSzqAqGLQhYq8MRL7MT27DN3mbtVz9iT2LPeO3Nn7A/8pCi+9z5zzrlnzvY8z3mG1DQNvkIh8JUWYLXiK8WUALn4f9tcMvPjuVTGNMJdXst7Vpo4ZhrBIlA1eDidke81hZa6EFKcFuoRADiD5WLM2Dz322+9eWaHWRHnO/+95tKuweY2AFBMIlmAcDpz+sswf5XRzwmSosZ4GWK8lP0X52U8Icjwza1rPu73226AJRYDPT7roZt73Xe+92XUFPd6/4tI867B5m0A8Ccz6BXBhggnlVWKomqQFGQ1JmRfflERMoiyWvCOfT4r9PttB3PX5JJn/zwwHPzlB2PRUcUEszl9KQ1zqcz9fjtdF8UoKuyNCfLla15S1PhyK4CEKOOapi+O3jngew8A3s1dY3npuu/4++dPv31mnjZD+G9vbY/et6mlFQBEM+gtxfkwd+735+b7kQvEBRkXZbVqWhtb7eqBze1bAOCT3L18bX7x4HDbSyxpTrKK87IbAEZMIbYc105EU+tCYQ6fTWVqUgqOAdx1lf/NpUqBYunabaW+u2tjgKuaU46OhQTAMGTWo7XSykdGVkfn0iJ4bWTNTn/9GqfstzOP598vZhoXR69ped5OEzUx7PVas0KHwvw9AGCpiVgeLsS5UZRd/fbaFENgALev870KAJ/nPyvqMw6WfHrvpuZ4LQy7PBYM/R2K8A4U26qlVQTDkzGuG/1hZwjcQmFVK+f6DqfotlJPFntWKphEdg82H3MyZFUMg05GoxfjFEqPUU7aWxWhIhBlZd9s8kosD1RpNSi23Nrv/TkAjBd9XuqDFpp4bvdgIFwN0+5Fa8nhfIS/GwCs1dDKA3Yhxu9ZqgmvjcSxKgih2OK10U+Xel4u/SR3bgwcNxpr0PjWJnbZvbEwbzfJnbZMxbjOpTcoAgenBTeUlpAqF2PLF6XGlM3LdoY8YTTWdHlYIPK+w7iQdad9RugUgyApe2dThSWRz2bM5bd0ukrGlhwqFSyx3YPNP3Cx+hl3eSxFfT4U5u6q0Z2wC3FubzHiLguJ6/UnmsBg+1rvS6ViSw4VKzkLTfzwgWtb5vQw9dkotYmliooYivA2ALhbD50S2DoZ49uLPUAW6rUSutzpxh532sGS36s0Tk+Jm7xnQ+Bpv42qOLDXay35vcUFBSLpTNXFniApo/NF3CgHv72yfBYSh5v7vCdQrVZprK7anybxF/7/utYL5caggNbpZssadCjCowBs08MzD/hUjNtTLi/bGRxnyfL+tK3PE7PSxDO6GOoUTBgZCDzW6WJLDuj2WIAkypMbC/MoxuzSyXMptk1EuWC5ARiGQcBBlnSnJpaA/+n1HAWAqB6GumeLBA6vPjgcPFfqea/XUtHHk6ICc6mM4WKPy8j7wlzl1UVfmZpme79vmiHx5/XyNDKNlrf1eQ6u9xcmFq+VBI+N1kVrLMyh2bbHAF9qMsrt0TWQwMFdJAj7rBRs6XKj9JzWy9To+sJbD93QdjL/Zq9PfxY+H+ZpANhpgOdtkzHOq3ew315YWvzvet+XBA4/NcDT+C7BtWuchzYF7Zev0ZSo061/8pyWVLiUFHVnp5Qo7Y3ykm76TpbAUa2SQ4uDhmvWOA8ji9dNpMrtk3ce2tz2Qe6iw2XJmrARhML8duSBOobSUzHeULBGQdhnuzKNGRnwf4YDvG5IwGr3lTa0Og5u6XBm/+4z4EY5nI/wqOjQs+1x+2SMcxmlH1isaTpcLFzd6ngUAAwv8VW7hvnRNzYH30ZTBa+Owi8fnKTCxYR4X6VxSUG6L2bAjXJgKBwcDK6ODPj+CgC/Nkyglp3IXr/tia8PBzVkutUgFMm6k7vMEHoqxlW9mTbU5sDXBuwHq/18Laven25f532jw8VoRBW6OR/JZqfdZYagbFROcUUhqxqMzQvqYND5bi17WjVtB1go4vEen1Xp91vBweibxOXASyrMJMSSxV5CkPbFBUOJBOK8rJ2a5sFnZ/Eur7VqawETNvX/3eZkfoayUofHgrc7GdXIcloozN+K6q8ij5ipGPd/eukoqgahsKiemxWxjKLByHr/bwDgY/2SFKLmDSSGJJ702ykR6cNlpXCUpWw0oWsddjzKU2rxudMdk1F92SglKtpnM7w2m5Kz77Ip6FDbXOyjRt8jH2bsrE0EnexPcjGYJnHo8rBYi4NWKxkPcqeLcaHAnRJ8ZjQhlncjVdNgMppRT18UMEHWsqyQDHes9/0CAE7V8kJgVn8MTeBHWhz05SXQbJFlp/E+f9Z6ysaesTB/CypQl9yyTkTLu1FSUNTPZniYTkjL5L+hwykGHEzN1gImNg7NtTqY4/kFMLNgPXhLE10y9oxHeVJRl2WnuyZj2b2oAqBYgqzk7CUB56Xl3krhGNy21ofmQ2O1v46JHVUkgX8/2MTM5t9fKNFLW48oazAd5/fnriNpcX8qU+hGSWEhliArKRbAbuxxp1xW6glz3sbcVrNUs509WmpFhCYWrKe1qTBzhSL8fwFAEADsUzF+2TaLqmowERXVM5euxJJ8MCQGt/R7jwNAwRdTLUztwcNx+FGrg54u9RxZj9e2kLms1JW9oPGogCsqoLgyMhnjLi8Tooxz6iKvzSTksnJu7XLHrDTxrImvYnpzohBwsM/SFUphlLm6vRY8l7kkRYOZhLAzyok7OUlZnnGk4laSA0VgcFOv5wUAiJn5IvmNQ2bAdikpnhqPCt16Bouyqk3FBOh0s5kmFsucnIzZx+ZF4CooJIdb+jypHRsCiNd8zZIvQT3aWdN+O/MspbMEZkgc6/FaMEFWmZMTCcfpGQHTqxSWxFFsOWG2UqBefb44Bq8EnUzJWJMPFHsuJkT4cCwORux3W6/H9NiSQ70aoAW/gznCGJh2/208rs2n9a+92GgCbup1H0MZvkoZy6JuneE4wMtBJxvSMzbBS3B2No2lJRV4SV9b8K39njmGIp6rVc5SqGfLvOS304f1NDr+ZTyea8yGuZRUcfnCyZKwtdt9FGV0UyQtgnqfJXit3cWcKTdAUlT4ZCJ+WY4oL+PoXjncttY7RRH4iybKWYB6K0b1WOnHrFRpNv+YSqjcEiPRsq3wpWON10rB5k73ERTHTJd2CRpx+uStdhf7abEHqIb6c6iwLpvnpGyRVwyLm2evmC/mcjRCMZrLQj3iYApb1j6fTauzaalABlkFiHJygWaCTQwMrXEeRB5YR3mzaNR5pT92uNh38m9+OFa68WA2lYH8qnzHVf6TOMAb9RFxORp2kMvGkIey3eKLmI4LaigilOQvKhqWFK9E4R6vBdY1V78dYhSNPOF2st3JvpW7KBZb8jGXzlyuEO8c8L8PAH+oo3zL0NCjfxaaeNRrpZSkIMO/ppMVeSdFFRMkBdYHbNDttR5qjJQLaPSZyDNtTub1TybioPdMFJomjAz4fwcAH9VbuKVo+GFRliIO22hC9+HLDS0Otd3FNtRaYIVO0Y7tv671FT0TTBwDODAc/BUA/L0hki3l3WiGkO3/p4/svDrAVxp3e79X6fBYCs4SNQIrde76wv1DLS+WmyqQOAZfGw6+BgBnGyrZIlbsQHqThTq6Z7A5Wer5jgFfpqWJMW07xChW8qT+/J5rmp9rKjJVQPFn/3WtLwOArvWcemBFf8LASpPH7h9qKTgTde/VAQ7FoZWRagEr/dsO8Z0bA894rVfa1VDc2TfU8gIAzKykYCutGGBI4sSB4eBE7vqBodZIk4V6amWlWgWKAQB+ZL3/cLuTAY+FhN2bmp/S2+9fT9Rjw60aEO+cnT8bF2TH6FBLD1LWSgu0WhQDsqI9rGiamyHxuuwTGcWqUQwAUIs/wrHi1gKrTDGrCqsh+K5K/CcAAP//FP712Xu+8jIAAAAASUVORK5CYII=", na = {
  data() {
    return {};
  }
}, Ft = window.Vue.createElementVNode, ra = window.Vue.openBlock, sa = window.Vue.createElementBlock, oa = { class: "text-center" }, ia = /* @__PURE__ */ Ft("h2", null, "404 Not found", -1), aa = /* @__PURE__ */ Ft("img", { src: sr }, null, -1), la = /* @__PURE__ */ Ft("h4", null, "Sorry, we couldn't find the url you're looking for!", -1), ca = [
  ia,
  aa,
  la
];
function ua(e, t, n, r, s, o) {
  return ra(), sa("div", oa, ca);
}
const da = /* @__PURE__ */ Me(na, [["render", ua]]), Ut = ws({
  history: Hr(),
  routes: [
    {
      path: "/",
      name: "Home",
      component: nr
    },
    {
      path: "/process/:name",
      name: "Process",
      component: rr,
      props: (e) => ({ processName: e.params.name })
    },
    {
      path: "/:pathMatch(.*)*",
      name: "NotFound",
      component: da
    }
  ]
}), yt = window.Quasar, fa = {
  name: "app",
  router: Ut,
  props: ["apiUrl", "readOnly"],
  data() {
    return {
      lang: "fr"
    };
  },
  methods: {
    changeLang: function() {
      yt.lang.set(this.lang == "fr" ? yt.lang.fr : yt.lang.en);
    }
  }
}, or = window.Vue.createElementVNode, ee = window.Vue.resolveComponent, ce = window.Vue.withCtx, Z = window.Vue.createVNode, yn = window.Vue.createTextVNode, ha = window.Vue.openBlock, pa = window.Vue.createBlock, ma = /* @__PURE__ */ or("img", { src: sr }, null, -1);
function ga(e, t, n, r, s, o) {
  const i = ee("q-avatar"), l = ee("q-toolbar-title"), c = ee("q-space"), d = ee("q-btn-toggle"), a = ee("q-btn"), u = ee("q-toolbar"), g = ee("q-header"), m = ee("router-view"), w = ee("q-page-container"), _ = ee("q-footer"), q = ee("q-layout");
  return ha(), pa(q, { view: "hHh LpR fFf" }, {
    default: ce(() => [
      Z(g, {
        elevated: "",
        class: "text-white gradient-bg"
      }, {
        default: ce(() => [
          Z(u, { class: "" }, {
            default: ce(() => [
              Z(l, null, {
                default: ce(() => [
                  or("div", null, [
                    Z(i, {
                      onClick: t[0] || (t[0] = (A) => e.$router.push("/"))
                    }, {
                      default: ce(() => [
                        ma
                      ]),
                      _: 1
                    }),
                    yn("Orchestra UI ")
                  ])
                ]),
                _: 1
              }),
              Z(c),
              Z(d, {
                modelValue: s.lang,
                "onUpdate:modelValue": t[1] || (t[1] = (A) => s.lang = A),
                options: [
                  { label: "Fr", value: "fr" },
                  { label: "En", value: "en" }
                ],
                onInput: t[2] || (t[2] = (A) => o.changeLang())
              }, null, 8, ["modelValue"]),
              Z(a, {
                flat: "",
                round: "",
                icon: "power_settings_new"
              })
            ]),
            _: 1
          })
        ]),
        _: 1
      }),
      Z(w, null, {
        default: ce(() => [
          Z(m, {
            "api-url": n.apiUrl,
            "orchestra-ui-read-only": n.readOnly
          }, null, 8, ["api-url", "orchestra-ui-read-only"])
        ]),
        _: 1
      }),
      Z(_, {
        elevated: "",
        class: "bg-grey-8 text-white"
      }, {
        default: ce(() => [
          Z(u, null, {
            default: ce(() => [
              Z(l, { class: "absolute-center" }, {
                default: ce(() => [
                  yn(" Copyright © 2020 - 2024 ")
                ]),
                _: 1
              })
            ]),
            _: 1
          })
        ]),
        _: 1
      })
    ]),
    _: 1
  });
}
const wa = /* @__PURE__ */ Me(fa, [["render", ga]]), ya = {
  name: "app",
  router: Ut,
  data() {
    return {
      left: !1
    };
  },
  props: ["apiUrl", "readOnly"]
}, _a = window.Vue.resolveComponent, ba = window.Vue.createVNode, va = window.Vue.openBlock, Ea = window.Vue.createElementBlock;
function Ra(e, t, n, r, s, o) {
  const i = _a("router-view");
  return va(), Ea("div", null, [
    ba(i, {
      "api-url": n.apiUrl,
      "orchestra-ui-read-only": n.readOnly
    }, null, 8, ["api-url", "orchestra-ui-read-only"])
  ]);
}
const xa = /* @__PURE__ */ Me(ya, [["render", Ra]]), Sa = {
  orchestra: {
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
  }
}, Oa = {
  orchestra: {
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
  }
}, Ee = window.Quasar;
var Ze = {
  //Quasar.lang.set(Quasar.lang.fr);
  install: function(e, t) {
    e.component("vui-orchestra-standalone", wa), e.component("vui-orchestra", xa), e.component("vui-orchestra-home", nr), e.component("vui-orchestra-process", rr);
  },
  lang: {
    enUS: Oa,
    fr: Sa
  }
};
Ee.lang.enUS && (Ee.lang.enUS.vuiOrchestra = { ...Ee.lang.enUs.vui, ...Ze.lang.enUS });
Ee.lang.fr && (Ee.lang.fr.vuiOrchestra = { ...Ee.lang.fr.vui, ...Ze.lang.fr });
window && (window.VertigoOrchestraUi = Ze, window.addEventListener("vui-before-plugins", function(e) {
  e.detail.vuiAppInstance.use(Ut).use(Ze);
}));
export {
  Ze as default
};
//# sourceMappingURL=vertigo-orchestra-ui.es.js.map
