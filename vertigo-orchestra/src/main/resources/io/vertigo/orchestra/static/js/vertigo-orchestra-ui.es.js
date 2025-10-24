/*!
  * vue-router v4.5.1
  * (c) 2025 Eduardo San Martin Morote
  * @license MIT
  */
window.Vue.getCurrentInstance;
const Ve = window.Vue.inject;
window.Vue.onUnmounted;
window.Vue.onDeactivated;
window.Vue.onActivated;
const ae = window.Vue.computed, qe = window.Vue.unref;
window.Vue.watchEffect;
const gn = window.Vue.defineComponent, ar = window.Vue.reactive, wn = window.Vue.h, dt = window.Vue.provide, lr = window.Vue.ref, cr = window.Vue.watch, ur = window.Vue.shallowRef, dr = window.Vue.shallowReactive, fr = window.Vue.nextTick, _e = typeof document < "u";
function yn(e) {
  return typeof e == "object" || "displayName" in e || "props" in e || "__vccOpts" in e;
}
function hr(e) {
  return e.__esModule || e[Symbol.toStringTag] === "Module" || // support CF with dynamic imports that do not
  // add the Module string tag
  e.default && yn(e.default);
}
const U = Object.assign;
function ft(e, t) {
  const n = {};
  for (const r in t) {
    const s = t[r];
    n[r] = ne(s) ? s.map(e) : e(s);
  }
  return n;
}
const Pe = () => {
}, ne = Array.isArray, vn = /#/g, pr = /&/g, mr = /\//g, gr = /=/g, wr = /\?/g, bn = /\+/g, yr = /%5B/g, vr = /%5D/g, _n = /%5E/g, br = /%60/g, En = /%7B/g, _r = /%7C/g, Rn = /%7D/g, Er = /%20/g;
function Tt(e) {
  return encodeURI("" + e).replace(_r, "|").replace(yr, "[").replace(vr, "]");
}
function Rr(e) {
  return Tt(e).replace(En, "{").replace(Rn, "}").replace(_n, "^");
}
function vt(e) {
  return Tt(e).replace(bn, "%2B").replace(Er, "+").replace(vn, "%23").replace(pr, "%26").replace(br, "`").replace(En, "{").replace(Rn, "}").replace(_n, "^");
}
function xr(e) {
  return vt(e).replace(gr, "%3D");
}
function Or(e) {
  return Tt(e).replace(vn, "%23").replace(wr, "%3F");
}
function Sr(e) {
  return e == null ? "" : Or(e).replace(mr, "%2F");
}
function Ue(e) {
  try {
    return decodeURIComponent("" + e);
  } catch {
  }
  return "" + e;
}
const Ar = /\/$/, Tr = (e) => e.replace(Ar, "");
function ht(e, t, n = "/") {
  let r, s = {}, o = "", i = "";
  const c = t.indexOf("#");
  let u = t.indexOf("?");
  return c < u && c >= 0 && (u = -1), u > -1 && (r = t.slice(0, u), o = t.slice(u + 1, c > -1 ? c : t.length), s = e(o)), c > -1 && (r = r || t.slice(0, c), i = t.slice(c, t.length)), r = Pr(r ?? t, n), {
    fullPath: r + (o && "?") + o + i,
    path: r,
    query: s,
    hash: Ue(i)
  };
}
function kr(e, t) {
  const n = t.query ? e(t.query) : "";
  return t.path + (n && "?") + n + (t.hash || "");
}
function Ft(e, t) {
  return !t || !e.toLowerCase().startsWith(t.toLowerCase()) ? e : e.slice(t.length) || "/";
}
function Cr(e, t, n) {
  const r = t.matched.length - 1, s = n.matched.length - 1;
  return r > -1 && r === s && Ee(t.matched[r], n.matched[s]) && xn(t.params, n.params) && e(t.query) === e(n.query) && t.hash === n.hash;
}
function Ee(e, t) {
  return (e.aliasOf || e) === (t.aliasOf || t);
}
function xn(e, t) {
  if (Object.keys(e).length !== Object.keys(t).length)
    return !1;
  for (const n in e)
    if (!qr(e[n], t[n]))
      return !1;
  return !0;
}
function qr(e, t) {
  return ne(e) ? Bt(e, t) : ne(t) ? Bt(t, e) : e === t;
}
function Bt(e, t) {
  return ne(t) ? e.length === t.length && e.every((n, r) => n === t[r]) : e.length === 1 && e[0] === t;
}
function Pr(e, t) {
  if (e.startsWith("/"))
    return e;
  if (!e)
    return t;
  const n = t.split("/"), r = e.split("/"), s = r[r.length - 1];
  (s === ".." || s === ".") && r.push("");
  let o = n.length - 1, i, c;
  for (i = 0; i < r.length; i++)
    if (c = r[i], c !== ".")
      if (c === "..")
        o > 1 && o--;
      else
        break;
  return n.slice(0, o).join("/") + "/" + r.slice(i).join("/");
}
const he = {
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
var Le;
(function(e) {
  e.pop = "pop", e.push = "push";
})(Le || (Le = {}));
var Ne;
(function(e) {
  e.back = "back", e.forward = "forward", e.unknown = "";
})(Ne || (Ne = {}));
function Nr(e) {
  if (!e)
    if (_e) {
      const t = document.querySelector("base");
      e = t && t.getAttribute("href") || "/", e = e.replace(/^\w+:\/\/[^\/]+/, "");
    } else
      e = "/";
  return e[0] !== "/" && e[0] !== "#" && (e = "/" + e), Tr(e);
}
const Vr = /^[^#]+#/;
function Ur(e, t) {
  return e.replace(Vr, "#") + t;
}
function Lr(e, t) {
  const n = document.documentElement.getBoundingClientRect(), r = e.getBoundingClientRect();
  return {
    behavior: t.behavior,
    left: r.left - n.left - (t.left || 0),
    top: r.top - n.top - (t.top || 0)
  };
}
const Ze = () => ({
  left: window.scrollX,
  top: window.scrollY
});
function Fr(e) {
  let t;
  if ("el" in e) {
    const n = e.el, r = typeof n == "string" && n.startsWith("#"), s = typeof n == "string" ? r ? document.getElementById(n.slice(1)) : document.querySelector(n) : n;
    if (!s)
      return;
    t = Lr(s, e);
  } else
    t = e;
  "scrollBehavior" in document.documentElement.style ? window.scrollTo(t) : window.scrollTo(t.left != null ? t.left : window.scrollX, t.top != null ? t.top : window.scrollY);
}
function Dt(e, t) {
  return (history.state ? history.state.position - t : -1) + e;
}
const bt = /* @__PURE__ */ new Map();
function Br(e, t) {
  bt.set(e, t);
}
function Dr(e) {
  const t = bt.get(e);
  return bt.delete(e), t;
}
let Ir = () => location.protocol + "//" + location.host;
function On(e, t) {
  const { pathname: n, search: r, hash: s } = t, o = e.indexOf("#");
  if (o > -1) {
    let c = s.includes(e.slice(o)) ? e.slice(o).length : 1, u = s.slice(c);
    return u[0] !== "/" && (u = "/" + u), Ft(u, "");
  }
  return Ft(n, e) + r + s;
}
function Mr(e, t, n, r) {
  let s = [], o = [], i = null;
  const c = ({ state: m }) => {
    const w = On(e, location), g = n.value, b = t.value;
    let v = 0;
    if (m) {
      if (n.value = w, t.value = m, i && i === g) {
        i = null;
        return;
      }
      v = b ? m.position - b.position : 0;
    } else
      r(w);
    s.forEach((O) => {
      O(n.value, g, {
        delta: v,
        type: Le.pop,
        direction: v ? v > 0 ? Ne.forward : Ne.back : Ne.unknown
      });
    });
  };
  function u() {
    i = n.value;
  }
  function d(m) {
    s.push(m);
    const w = () => {
      const g = s.indexOf(m);
      g > -1 && s.splice(g, 1);
    };
    return o.push(w), w;
  }
  function a() {
    const { history: m } = window;
    m.state && m.replaceState(U({}, m.state, { scroll: Ze() }), "");
  }
  function l() {
    for (const m of o)
      m();
    o = [], window.removeEventListener("popstate", c), window.removeEventListener("beforeunload", a);
  }
  return window.addEventListener("popstate", c), window.addEventListener("beforeunload", a, {
    passive: !0
  }), {
    pauseListeners: u,
    listen: d,
    destroy: l
  };
}
function It(e, t, n, r = !1, s = !1) {
  return {
    back: e,
    current: t,
    forward: n,
    replaced: r,
    position: window.history.length,
    scroll: s ? Ze() : null
  };
}
function $r(e) {
  const { history: t, location: n } = window, r = {
    value: On(e, n)
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
  function o(u, d, a) {
    const l = e.indexOf("#"), m = l > -1 ? (n.host && document.querySelector("base") ? e : e.slice(l)) + u : Ir() + e + u;
    try {
      t[a ? "replaceState" : "pushState"](d, "", m), s.value = d;
    } catch (w) {
      console.error(w), n[a ? "replace" : "assign"](m);
    }
  }
  function i(u, d) {
    const a = U({}, t.state, It(
      s.value.back,
      // keep back and forward entries but override current position
      u,
      s.value.forward,
      !0
    ), d, { position: s.value.position });
    o(u, a, !0), r.value = u;
  }
  function c(u, d) {
    const a = U(
      {},
      // use current history state to gracefully handle a wrong call to
      // history.replaceState
      // https://github.com/vuejs/router/issues/366
      s.value,
      t.state,
      {
        forward: u,
        scroll: Ze()
      }
    );
    o(a.current, a, !0);
    const l = U({}, It(r.value, u, null), { position: a.position + 1 }, d);
    o(u, l, !1), r.value = u;
  }
  return {
    location: r,
    state: s,
    push: c,
    replace: i
  };
}
function jr(e) {
  e = Nr(e);
  const t = $r(e), n = Mr(e, t.state, t.location, t.replace);
  function r(o, i = !0) {
    i || n.pauseListeners(), history.go(o);
  }
  const s = U({
    // it's overridden right after
    location: "",
    base: e,
    go: r,
    createHref: Ur.bind(null, e)
  }, t, n);
  return Object.defineProperty(s, "location", {
    enumerable: !0,
    get: () => t.location.value
  }), Object.defineProperty(s, "state", {
    enumerable: !0,
    get: () => t.state.value
  }), s;
}
function Wr(e) {
  return e = location.host ? e || location.pathname + location.search : "", e.includes("#") || (e += "#"), jr(e);
}
function zr(e) {
  return typeof e == "string" || e && typeof e == "object";
}
function Sn(e) {
  return typeof e == "string" || typeof e == "symbol";
}
const An = Symbol("");
var Mt;
(function(e) {
  e[e.aborted = 4] = "aborted", e[e.cancelled = 8] = "cancelled", e[e.duplicated = 16] = "duplicated";
})(Mt || (Mt = {}));
function Re(e, t) {
  return U(new Error(), {
    type: e,
    [An]: !0
  }, t);
}
function ce(e, t) {
  return e instanceof Error && An in e && (t == null || !!(e.type & t));
}
const $t = "[^/]+?", Hr = {
  sensitive: !1,
  strict: !1,
  start: !0,
  end: !0
}, Kr = /[.+*?^${}()[\]/\\]/g;
function Yr(e, t) {
  const n = U({}, Hr, t), r = [];
  let s = n.start ? "^" : "";
  const o = [];
  for (const d of e) {
    const a = d.length ? [] : [
      90
      /* PathScore.Root */
    ];
    n.strict && !d.length && (s += "/");
    for (let l = 0; l < d.length; l++) {
      const m = d[l];
      let w = 40 + (n.sensitive ? 0.25 : 0);
      if (m.type === 0)
        l || (s += "/"), s += m.value.replace(Kr, "\\$&"), w += 40;
      else if (m.type === 1) {
        const { value: g, repeatable: b, optional: v, regexp: O } = m;
        o.push({
          name: g,
          repeatable: b,
          optional: v
        });
        const R = O || $t;
        if (R !== $t) {
          w += 10;
          try {
            new RegExp(`(${R})`);
          } catch (I) {
            throw new Error(`Invalid custom RegExp for param "${g}" (${R}): ` + I.message);
          }
        }
        let A = b ? `((?:${R})(?:/(?:${R}))*)` : `(${R})`;
        l || (A = // avoid an optional / if there are more segments e.g. /:p?-static
        // or /:p?-:p2
        v && d.length < 2 ? `(?:/${A})` : "/" + A), v && (A += "?"), s += A, w += 20, v && (w += -8), b && (w += -20), R === ".*" && (w += -50);
      }
      a.push(w);
    }
    r.push(a);
  }
  if (n.strict && n.end) {
    const d = r.length - 1;
    r[d][r[d].length - 1] += 0.7000000000000001;
  }
  n.strict || (s += "/?"), n.end ? s += "$" : n.strict && !s.endsWith("/") && (s += "(?:/|$)");
  const i = new RegExp(s, n.sensitive ? "" : "i");
  function c(d) {
    const a = d.match(i), l = {};
    if (!a)
      return null;
    for (let m = 1; m < a.length; m++) {
      const w = a[m] || "", g = o[m - 1];
      l[g.name] = w && g.repeatable ? w.split("/") : w;
    }
    return l;
  }
  function u(d) {
    let a = "", l = !1;
    for (const m of e) {
      (!l || !a.endsWith("/")) && (a += "/"), l = !1;
      for (const w of m)
        if (w.type === 0)
          a += w.value;
        else if (w.type === 1) {
          const { value: g, repeatable: b, optional: v } = w, O = g in d ? d[g] : "";
          if (ne(O) && !b)
            throw new Error(`Provided param "${g}" is an array but it is not repeatable (* or + modifiers)`);
          const R = ne(O) ? O.join("/") : O;
          if (!R)
            if (v)
              m.length < 2 && (a.endsWith("/") ? a = a.slice(0, -1) : l = !0);
            else
              throw new Error(`Missing required param "${g}"`);
          a += R;
        }
    }
    return a || "/";
  }
  return {
    re: i,
    score: r,
    keys: o,
    parse: c,
    stringify: u
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
function Tn(e, t) {
  let n = 0;
  const r = e.score, s = t.score;
  for (; n < r.length && n < s.length; ) {
    const o = Jr(r[n], s[n]);
    if (o)
      return o;
    n++;
  }
  if (Math.abs(s.length - r.length) === 1) {
    if (jt(r))
      return 1;
    if (jt(s))
      return -1;
  }
  return s.length - r.length;
}
function jt(e) {
  const t = e[e.length - 1];
  return e.length > 0 && t[t.length - 1] < 0;
}
const Gr = {
  type: 0,
  value: ""
}, Qr = /[a-zA-Z0-9_]/;
function Zr(e) {
  if (!e)
    return [[]];
  if (e === "/")
    return [[Gr]];
  if (!e.startsWith("/"))
    throw new Error(`Invalid path "${e}"`);
  function t(w) {
    throw new Error(`ERR (${n})/"${d}": ${w}`);
  }
  let n = 0, r = n;
  const s = [];
  let o;
  function i() {
    o && s.push(o), o = [];
  }
  let c = 0, u, d = "", a = "";
  function l() {
    d && (n === 0 ? o.push({
      type: 0,
      value: d
    }) : n === 1 || n === 2 || n === 3 ? (o.length > 1 && (u === "*" || u === "+") && t(`A repeatable param (${d}) must be alone in its segment. eg: '/:ids+.`), o.push({
      type: 1,
      value: d,
      regexp: a,
      repeatable: u === "*" || u === "+",
      optional: u === "*" || u === "?"
    })) : t("Invalid state to consume buffer"), d = "");
  }
  function m() {
    d += u;
  }
  for (; c < e.length; ) {
    if (u = e[c++], u === "\\" && n !== 2) {
      r = n, n = 4;
      continue;
    }
    switch (n) {
      case 0:
        u === "/" ? (d && l(), i()) : u === ":" ? (l(), n = 1) : m();
        break;
      case 4:
        m(), n = r;
        break;
      case 1:
        u === "(" ? n = 2 : Qr.test(u) ? m() : (l(), n = 0, u !== "*" && u !== "?" && u !== "+" && c--);
        break;
      case 2:
        u === ")" ? a[a.length - 1] == "\\" ? a = a.slice(0, -1) + u : n = 3 : a += u;
        break;
      case 3:
        l(), n = 0, u !== "*" && u !== "?" && u !== "+" && c--, a = "";
        break;
      default:
        t("Unknown state");
        break;
    }
  }
  return n === 2 && t(`Unfinished custom RegExp for param "${d}"`), l(), i(), s;
}
function Xr(e, t, n) {
  const r = Yr(Zr(e.path), n), s = U(r, {
    record: e,
    parent: t,
    // these needs to be populated by the parent
    children: [],
    alias: []
  });
  return t && !s.record.aliasOf == !t.record.aliasOf && t.children.push(s), s;
}
function es(e, t) {
  const n = [], r = /* @__PURE__ */ new Map();
  t = Kt({ strict: !1, end: !0, sensitive: !1 }, t);
  function s(l) {
    return r.get(l);
  }
  function o(l, m, w) {
    const g = !w, b = zt(l);
    b.aliasOf = w && w.record;
    const v = Kt(t, l), O = [b];
    if ("alias" in l) {
      const I = typeof l.alias == "string" ? [l.alias] : l.alias;
      for (const B of I)
        O.push(
          // we need to normalize again to ensure the `mods` property
          // being non enumerable
          zt(U({}, b, {
            // this allows us to hold a copy of the `components` option
            // so that async components cache is hold on the original record
            components: w ? w.record.components : b.components,
            path: B,
            // we might be the child of an alias
            aliasOf: w ? w.record : b
            // the aliases are always of the same kind as the original since they
            // are defined on the same record
          }))
        );
    }
    let R, A;
    for (const I of O) {
      const { path: B } = I;
      if (m && B[0] !== "/") {
        const j = m.record.path, M = j[j.length - 1] === "/" ? "" : "/";
        I.path = m.record.path + (B && M + B);
      }
      if (R = Xr(I, m, v), w ? w.alias.push(R) : (A = A || R, A !== R && A.alias.push(R), g && l.name && !Ht(R) && i(l.name)), kn(R) && u(R), b.children) {
        const j = b.children;
        for (let M = 0; M < j.length; M++)
          o(j[M], R, w && w.children[M]);
      }
      w = w || R;
    }
    return A ? () => {
      i(A);
    } : Pe;
  }
  function i(l) {
    if (Sn(l)) {
      const m = r.get(l);
      m && (r.delete(l), n.splice(n.indexOf(m), 1), m.children.forEach(i), m.alias.forEach(i));
    } else {
      const m = n.indexOf(l);
      m > -1 && (n.splice(m, 1), l.record.name && r.delete(l.record.name), l.children.forEach(i), l.alias.forEach(i));
    }
  }
  function c() {
    return n;
  }
  function u(l) {
    const m = rs(l, n);
    n.splice(m, 0, l), l.record.name && !Ht(l) && r.set(l.record.name, l);
  }
  function d(l, m) {
    let w, g = {}, b, v;
    if ("name" in l && l.name) {
      if (w = r.get(l.name), !w)
        throw Re(1, {
          location: l
        });
      v = w.record.name, g = U(
        // paramsFromLocation is a new object
        Wt(
          m.params,
          // only keep params that exist in the resolved location
          // only keep optional params coming from a parent record
          w.keys.filter((A) => !A.optional).concat(w.parent ? w.parent.keys.filter((A) => A.optional) : []).map((A) => A.name)
        ),
        // discard any existing params in the current location that do not exist here
        // #1497 this ensures better active/exact matching
        l.params && Wt(l.params, w.keys.map((A) => A.name))
      ), b = w.stringify(g);
    } else if (l.path != null)
      b = l.path, w = n.find((A) => A.re.test(b)), w && (g = w.parse(b), v = w.record.name);
    else {
      if (w = m.name ? r.get(m.name) : n.find((A) => A.re.test(m.path)), !w)
        throw Re(1, {
          location: l,
          currentLocation: m
        });
      v = w.record.name, g = U({}, m.params, l.params), b = w.stringify(g);
    }
    const O = [];
    let R = w;
    for (; R; )
      O.unshift(R.record), R = R.parent;
    return {
      name: v,
      path: b,
      params: g,
      matched: O,
      meta: ns(O)
    };
  }
  e.forEach((l) => o(l));
  function a() {
    n.length = 0, r.clear();
  }
  return {
    addRoute: o,
    resolve: d,
    removeRoute: i,
    clearRoutes: a,
    getRoutes: c,
    getRecordMatcher: s
  };
}
function Wt(e, t) {
  const n = {};
  for (const r of t)
    r in e && (n[r] = e[r]);
  return n;
}
function zt(e) {
  const t = {
    path: e.path,
    redirect: e.redirect,
    name: e.name,
    meta: e.meta || {},
    aliasOf: e.aliasOf,
    beforeEnter: e.beforeEnter,
    props: ts(e),
    children: e.children || [],
    instances: {},
    leaveGuards: /* @__PURE__ */ new Set(),
    updateGuards: /* @__PURE__ */ new Set(),
    enterCallbacks: {},
    // must be declared afterwards
    // mods: {},
    components: "components" in e ? e.components || null : e.component && { default: e.component }
  };
  return Object.defineProperty(t, "mods", {
    value: {}
  }), t;
}
function ts(e) {
  const t = {}, n = e.props || !1;
  if ("component" in e)
    t.default = n;
  else
    for (const r in e.components)
      t[r] = typeof n == "object" ? n[r] : n;
  return t;
}
function Ht(e) {
  for (; e; ) {
    if (e.record.aliasOf)
      return !0;
    e = e.parent;
  }
  return !1;
}
function ns(e) {
  return e.reduce((t, n) => U(t, n.meta), {});
}
function Kt(e, t) {
  const n = {};
  for (const r in e)
    n[r] = r in t ? t[r] : e[r];
  return n;
}
function rs(e, t) {
  let n = 0, r = t.length;
  for (; n !== r; ) {
    const o = n + r >> 1;
    Tn(e, t[o]) < 0 ? r = o : n = o + 1;
  }
  const s = ss(e);
  return s && (r = t.lastIndexOf(s, r - 1)), r;
}
function ss(e) {
  let t = e;
  for (; t = t.parent; )
    if (kn(t) && Tn(e, t) === 0)
      return t;
}
function kn({ record: e }) {
  return !!(e.name || e.components && Object.keys(e.components).length || e.redirect);
}
function os(e) {
  const t = {};
  if (e === "" || e === "?")
    return t;
  const r = (e[0] === "?" ? e.slice(1) : e).split("&");
  for (let s = 0; s < r.length; ++s) {
    const o = r[s].replace(bn, " "), i = o.indexOf("="), c = Ue(i < 0 ? o : o.slice(0, i)), u = i < 0 ? null : Ue(o.slice(i + 1));
    if (c in t) {
      let d = t[c];
      ne(d) || (d = t[c] = [d]), d.push(u);
    } else
      t[c] = u;
  }
  return t;
}
function Yt(e) {
  let t = "";
  for (let n in e) {
    const r = e[n];
    if (n = xr(n), r == null) {
      r !== void 0 && (t += (t.length ? "&" : "") + n);
      continue;
    }
    (ne(r) ? r.map((o) => o && vt(o)) : [r && vt(r)]).forEach((o) => {
      o !== void 0 && (t += (t.length ? "&" : "") + n, o != null && (t += "=" + o));
    });
  }
  return t;
}
function is(e) {
  const t = {};
  for (const n in e) {
    const r = e[n];
    r !== void 0 && (t[n] = ne(r) ? r.map((s) => s == null ? null : "" + s) : r == null ? r : "" + r);
  }
  return t;
}
const as = Symbol(""), Jt = Symbol(""), kt = Symbol(""), Cn = Symbol(""), _t = Symbol("");
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
function ge(e, t, n, r, s, o = (i) => i()) {
  const i = r && // name is defined if record is because of the function overload
  (r.enterCallbacks[s] = r.enterCallbacks[s] || []);
  return () => new Promise((c, u) => {
    const d = (m) => {
      m === !1 ? u(Re(4, {
        from: n,
        to: t
      })) : m instanceof Error ? u(m) : zr(m) ? u(Re(2, {
        from: t,
        to: m
      })) : (i && // since enterCallbackArray is truthy, both record and name also are
      r.enterCallbacks[s] === i && typeof m == "function" && i.push(m), c());
    }, a = o(() => e.call(r && r.instances[s], t, n, d));
    let l = Promise.resolve(a);
    e.length < 3 && (l = l.then(d)), l.catch((m) => u(m));
  });
}
function pt(e, t, n, r, s = (o) => o()) {
  const o = [];
  for (const i of e)
    for (const c in i.components) {
      let u = i.components[c];
      if (!(t !== "beforeRouteEnter" && !i.instances[c]))
        if (yn(u)) {
          const a = (u.__vccOpts || u)[t];
          a && o.push(ge(a, n, r, i, c, s));
        } else {
          let d = u();
          o.push(() => d.then((a) => {
            if (!a)
              throw new Error(`Couldn't resolve component "${c}" at "${i.path}"`);
            const l = hr(a) ? a.default : a;
            i.mods[c] = a, i.components[c] = l;
            const w = (l.__vccOpts || l)[t];
            return w && ge(w, n, r, i, c, s)();
          }));
        }
    }
  return o;
}
function Gt(e) {
  const t = Ve(kt), n = Ve(Cn), r = ae(() => {
    const u = qe(e.to);
    return t.resolve(u);
  }), s = ae(() => {
    const { matched: u } = r.value, { length: d } = u, a = u[d - 1], l = n.matched;
    if (!a || !l.length)
      return -1;
    const m = l.findIndex(Ee.bind(null, a));
    if (m > -1)
      return m;
    const w = Qt(u[d - 2]);
    return (
      // we are dealing with nested routes
      d > 1 && // if the parent and matched route have the same path, this link is
      // referring to the empty child. Or we currently are on a different
      // child of the same parent
      Qt(a) === w && // avoid comparing the child with its parent
      l[l.length - 1].path !== w ? l.findIndex(Ee.bind(null, u[d - 2])) : m
    );
  }), o = ae(() => s.value > -1 && fs(n.params, r.value.params)), i = ae(() => s.value > -1 && s.value === n.matched.length - 1 && xn(n.params, r.value.params));
  function c(u = {}) {
    if (ds(u)) {
      const d = t[qe(e.replace) ? "replace" : "push"](
        qe(e.to)
        // avoid uncaught errors are they are logged anyway
      ).catch(Pe);
      return e.viewTransition && typeof document < "u" && "startViewTransition" in document && document.startViewTransition(() => d), d;
    }
    return Promise.resolve();
  }
  return {
    route: r,
    href: ae(() => r.value.href),
    isActive: o,
    isExactActive: i,
    navigate: c
  };
}
function ls(e) {
  return e.length === 1 ? e[0] : e;
}
const cs = /* @__PURE__ */ gn({
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
    },
    viewTransition: Boolean
  },
  useLink: Gt,
  setup(e, { slots: t }) {
    const n = ar(Gt(e)), { options: r } = Ve(kt), s = ae(() => ({
      [Zt(e.activeClass, r.linkActiveClass, "router-link-active")]: n.isActive,
      // [getLinkClass(
      //   props.inactiveClass,
      //   options.linkInactiveClass,
      //   'router-link-inactive'
      // )]: !link.isExactActive,
      [Zt(e.exactActiveClass, r.linkExactActiveClass, "router-link-exact-active")]: n.isExactActive
    }));
    return () => {
      const o = t.default && ls(t.default(n));
      return e.custom ? o : wn("a", {
        "aria-current": n.isExactActive ? e.ariaCurrentValue : null,
        href: n.href,
        // this would override user added attrs but Vue will still add
        // the listener, so we end up triggering both
        onClick: n.navigate,
        class: s.value
      }, o);
    };
  }
}), us = cs;
function ds(e) {
  if (!(e.metaKey || e.altKey || e.ctrlKey || e.shiftKey) && !e.defaultPrevented && !(e.button !== void 0 && e.button !== 0)) {
    if (e.currentTarget && e.currentTarget.getAttribute) {
      const t = e.currentTarget.getAttribute("target");
      if (/\b_blank\b/i.test(t))
        return;
    }
    return e.preventDefault && e.preventDefault(), !0;
  }
}
function fs(e, t) {
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
function Qt(e) {
  return e ? e.aliasOf ? e.aliasOf.path : e.path : "";
}
const Zt = (e, t, n) => e ?? t ?? n, hs = /* @__PURE__ */ gn({
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
    const r = Ve(_t), s = ae(() => e.route || r.value), o = Ve(Jt, 0), i = ae(() => {
      let d = qe(o);
      const { matched: a } = s.value;
      let l;
      for (; (l = a[d]) && !l.components; )
        d++;
      return d;
    }), c = ae(() => s.value.matched[i.value]);
    dt(Jt, ae(() => i.value + 1)), dt(as, c), dt(_t, s);
    const u = lr();
    return cr(() => [u.value, c.value, e.name], ([d, a, l], [m, w, g]) => {
      a && (a.instances[l] = d, w && w !== a && d && d === m && (a.leaveGuards.size || (a.leaveGuards = w.leaveGuards), a.updateGuards.size || (a.updateGuards = w.updateGuards))), d && a && // if there is no instance but to and from are the same this might be
      // the first visit
      (!w || !Ee(a, w) || !m) && (a.enterCallbacks[l] || []).forEach((b) => b(d));
    }, { flush: "post" }), () => {
      const d = s.value, a = e.name, l = c.value, m = l && l.components[a];
      if (!m)
        return Xt(n.default, { Component: m, route: d });
      const w = l.props[a], g = w ? w === !0 ? d.params : typeof w == "function" ? w(d) : w : null, v = wn(m, U({}, g, t, {
        onVnodeUnmounted: (O) => {
          O.component.isUnmounted && (l.instances[a] = null);
        },
        ref: u
      }));
      return (
        // pass the vnode to the slot as a prop.
        // h and <component :is="..."> both accept vnodes
        Xt(n.default, { Component: v, route: d }) || v
      );
    };
  }
});
function Xt(e, t) {
  if (!e)
    return null;
  const n = e(t);
  return n.length === 1 ? n[0] : n;
}
const ps = hs;
function ms(e) {
  const t = es(e.routes, e), n = e.parseQuery || os, r = e.stringifyQuery || Yt, s = e.history, o = Ae(), i = Ae(), c = Ae(), u = ur(he);
  let d = he;
  _e && e.scrollBehavior && "scrollRestoration" in history && (history.scrollRestoration = "manual");
  const a = ft.bind(null, (y) => "" + y), l = ft.bind(null, Sr), m = (
    // @ts-expect-error: intentionally avoid the type check
    ft.bind(null, Ue)
  );
  function w(y, E) {
    let _, S;
    return Sn(y) ? (_ = t.getRecordMatcher(y), S = E) : S = y, t.addRoute(S, _);
  }
  function g(y) {
    const E = t.getRecordMatcher(y);
    E && t.removeRoute(E);
  }
  function b() {
    return t.getRoutes().map((y) => y.record);
  }
  function v(y) {
    return !!t.getRecordMatcher(y);
  }
  function O(y, E) {
    if (E = U({}, E || u.value), typeof y == "string") {
      const C = ht(n, y, E.path), z = t.resolve({ path: C.path }, E), Se = s.createHref(C.fullPath);
      return U(C, z, {
        params: m(z.params),
        hash: Ue(C.hash),
        redirectedFrom: void 0,
        href: Se
      });
    }
    let _;
    if (y.path != null)
      _ = U({}, y, {
        path: ht(n, y.path, E.path).path
      });
    else {
      const C = U({}, y.params);
      for (const z in C)
        C[z] == null && delete C[z];
      _ = U({}, y, {
        params: l(C)
      }), E.params = l(E.params);
    }
    const S = t.resolve(_, E), D = y.hash || "";
    S.params = a(m(S.params));
    const W = kr(r, U({}, y, {
      hash: Rr(D),
      path: S.path
    })), N = s.createHref(W);
    return U({
      fullPath: W,
      // keep the hash encoded so fullPath is effectively path + encodedQuery +
      // hash
      hash: D,
      query: (
        // if the user is using a custom query lib like qs, we might have
        // nested objects, so we keep the query as is, meaning it can contain
        // numbers at `$route.query`, but at the point, the user will have to
        // use their own type anyway.
        // https://github.com/vuejs/router/issues/328#issuecomment-649481567
        r === Yt ? is(y.query) : y.query || {}
      )
    }, S, {
      redirectedFrom: void 0,
      href: N
    });
  }
  function R(y) {
    return typeof y == "string" ? ht(n, y, u.value.path) : U({}, y);
  }
  function A(y, E) {
    if (d !== y)
      return Re(8, {
        from: E,
        to: y
      });
  }
  function I(y) {
    return M(y);
  }
  function B(y) {
    return I(U(R(y), { replace: !0 }));
  }
  function j(y) {
    const E = y.matched[y.matched.length - 1];
    if (E && E.redirect) {
      const { redirect: _ } = E;
      let S = typeof _ == "function" ? _(y) : _;
      return typeof S == "string" && (S = S.includes("?") || S.includes("#") ? S = R(S) : (
        // force empty params
        { path: S }
      ), S.params = {}), U({
        query: y.query,
        hash: y.hash,
        // avoid transferring params if the redirect has a path
        params: S.path != null ? {} : y.params
      }, S);
    }
  }
  function M(y, E) {
    const _ = d = O(y), S = u.value, D = y.state, W = y.force, N = y.replace === !0, C = j(_);
    if (C)
      return M(
        U(R(C), {
          state: typeof C == "object" ? U({}, D, C.state) : D,
          force: W,
          replace: N
        }),
        // keep original redirectedFrom if it exists
        E || _
      );
    const z = _;
    z.redirectedFrom = E;
    let Se;
    return !W && Cr(r, S, _) && (Se = Re(16, { to: z, from: S }), Ut(
      S,
      S,
      // this is a push, the only way for it to be triggered from a
      // history.listen is with a redirect, which makes it become a push
      !0,
      // This cannot be the first navigation because the initial location
      // cannot be manually navigated to
      !1
    )), (Se ? Promise.resolve(Se) : Me(z, S)).catch((G) => ce(G) ? (
      // navigation redirects still mark the router as ready
      ce(
        G,
        2
        /* ErrorTypes.NAVIGATION_GUARD_REDIRECT */
      ) ? G : lt(G)
    ) : (
      // reject any unknown error
      at(G, z, S)
    )).then((G) => {
      if (G) {
        if (ce(
          G,
          2
          /* ErrorTypes.NAVIGATION_GUARD_REDIRECT */
        ))
          return M(
            // keep options
            U({
              // preserve an existing replacement but allow the redirect to override it
              replace: N
            }, R(G.to), {
              state: typeof G.to == "object" ? U({}, D, G.to.state) : D,
              force: W
            }),
            // preserve the original redirectedFrom if any
            E || z
          );
      } else
        G = P(z, S, !0, N, D);
      return T(z, S, G), G;
    });
  }
  function fe(y, E) {
    const _ = A(y, E);
    return _ ? Promise.reject(_) : Promise.resolve();
  }
  function le(y) {
    const E = je.values().next().value;
    return E && typeof E.runWithContext == "function" ? E.runWithContext(y) : y();
  }
  function Me(y, E) {
    let _;
    const [S, D, W] = gs(y, E);
    _ = pt(S.reverse(), "beforeRouteLeave", y, E);
    for (const C of S)
      C.leaveGuards.forEach((z) => {
        _.push(ge(z, y, E));
      });
    const N = fe.bind(null, y, E);
    return _.push(N), be(_).then(() => {
      _ = [];
      for (const C of o.list())
        _.push(ge(C, y, E));
      return _.push(N), be(_);
    }).then(() => {
      _ = pt(D, "beforeRouteUpdate", y, E);
      for (const C of D)
        C.updateGuards.forEach((z) => {
          _.push(ge(z, y, E));
        });
      return _.push(N), be(_);
    }).then(() => {
      _ = [];
      for (const C of W)
        if (C.beforeEnter)
          if (ne(C.beforeEnter))
            for (const z of C.beforeEnter)
              _.push(ge(z, y, E));
          else
            _.push(ge(C.beforeEnter, y, E));
      return _.push(N), be(_);
    }).then(() => (y.matched.forEach((C) => C.enterCallbacks = {}), _ = pt(W, "beforeRouteEnter", y, E, le), _.push(N), be(_))).then(() => {
      _ = [];
      for (const C of i.list())
        _.push(ge(C, y, E));
      return _.push(N), be(_);
    }).catch((C) => ce(
      C,
      8
      /* ErrorTypes.NAVIGATION_CANCELLED */
    ) ? C : Promise.reject(C));
  }
  function T(y, E, _) {
    c.list().forEach((S) => le(() => S(y, E, _)));
  }
  function P(y, E, _, S, D) {
    const W = A(y, E);
    if (W)
      return W;
    const N = E === he, C = _e ? history.state : {};
    _ && (S || N ? s.replace(y.fullPath, U({
      scroll: N && C && C.scroll
    }, D)) : s.push(y.fullPath, D)), u.value = y, Ut(y, E, _, N), lt();
  }
  let se;
  function or() {
    se || (se = s.listen((y, E, _) => {
      if (!Lt.listening)
        return;
      const S = O(y), D = j(S);
      if (D) {
        M(U(D, { replace: !0, force: !0 }), S).catch(Pe);
        return;
      }
      d = S;
      const W = u.value;
      _e && Br(Dt(W.fullPath, _.delta), Ze()), Me(S, W).catch((N) => ce(
        N,
        12
        /* ErrorTypes.NAVIGATION_CANCELLED */
      ) ? N : ce(
        N,
        2
        /* ErrorTypes.NAVIGATION_GUARD_REDIRECT */
      ) ? (M(
        U(R(N.to), {
          force: !0
        }),
        S
        // avoid an uncaught rejection, let push call triggerError
      ).then((C) => {
        ce(
          C,
          20
          /* ErrorTypes.NAVIGATION_DUPLICATED */
        ) && !_.delta && _.type === Le.pop && s.go(-1, !1);
      }).catch(Pe), Promise.reject()) : (_.delta && s.go(-_.delta, !1), at(N, S, W))).then((N) => {
        N = N || P(
          // after navigation, all matched components are resolved
          S,
          W,
          !1
        ), N && (_.delta && // a new navigation has been triggered, so we do not want to revert, that will change the current history
        // entry while a different route is displayed
        !ce(
          N,
          8
          /* ErrorTypes.NAVIGATION_CANCELLED */
        ) ? s.go(-_.delta, !1) : _.type === Le.pop && ce(
          N,
          20
          /* ErrorTypes.NAVIGATION_DUPLICATED */
        ) && s.go(-1, !1)), T(S, W, N);
      }).catch(Pe);
    }));
  }
  let it = Ae(), Vt = Ae(), $e;
  function at(y, E, _) {
    lt(y);
    const S = Vt.list();
    return S.length ? S.forEach((D) => D(y, E, _)) : console.error(y), Promise.reject(y);
  }
  function ir() {
    return $e && u.value !== he ? Promise.resolve() : new Promise((y, E) => {
      it.add([y, E]);
    });
  }
  function lt(y) {
    return $e || ($e = !y, or(), it.list().forEach(([E, _]) => y ? _(y) : E()), it.reset()), y;
  }
  function Ut(y, E, _, S) {
    const { scrollBehavior: D } = e;
    if (!_e || !D)
      return Promise.resolve();
    const W = !_ && Dr(Dt(y.fullPath, 0)) || (S || !_) && history.state && history.state.scroll || null;
    return fr().then(() => D(y, E, W)).then((N) => N && Fr(N)).catch((N) => at(N, y, E));
  }
  const ct = (y) => s.go(y);
  let ut;
  const je = /* @__PURE__ */ new Set(), Lt = {
    currentRoute: u,
    listening: !0,
    addRoute: w,
    removeRoute: g,
    clearRoutes: t.clearRoutes,
    hasRoute: v,
    getRoutes: b,
    resolve: O,
    options: e,
    push: I,
    replace: B,
    go: ct,
    back: () => ct(-1),
    forward: () => ct(1),
    beforeEach: o.add,
    beforeResolve: i.add,
    afterEach: c.add,
    onError: Vt.add,
    isReady: ir,
    install(y) {
      const E = this;
      y.component("RouterLink", us), y.component("RouterView", ps), y.config.globalProperties.$router = E, Object.defineProperty(y.config.globalProperties, "$route", {
        enumerable: !0,
        get: () => qe(u)
      }), _e && // used for the initial navigation client side to avoid pushing
      // multiple times when the router is used in multiple apps
      !ut && u.value === he && (ut = !0, I(s.location).catch((D) => {
      }));
      const _ = {};
      for (const D in he)
        Object.defineProperty(_, D, {
          get: () => u.value[D],
          enumerable: !0
        });
      y.provide(kt, E), y.provide(Cn, dr(_)), y.provide(_t, u);
      const S = y.unmount;
      je.add(y), y.unmount = function() {
        je.delete(y), je.size < 1 && (d = he, se && se(), se = null, u.value = he, ut = !1, $e = !1), S();
      };
    }
  };
  function be(y) {
    return y.reduce((E, _) => E.then(() => le(_)), Promise.resolve());
  }
  return Lt;
}
function gs(e, t) {
  const n = [], r = [], s = [], o = Math.max(t.matched.length, e.matched.length);
  for (let i = 0; i < o; i++) {
    const c = t.matched[i];
    c && (e.matched.find((d) => Ee(d, c)) ? r.push(c) : n.push(c));
    const u = e.matched[i];
    u && (t.matched.find((d) => Ee(d, u)) || s.push(u));
  }
  return [n, r, s];
}
function qn(e, t) {
  return function() {
    return e.apply(t, arguments);
  };
}
const { toString: ws } = Object.prototype, { getPrototypeOf: Ct } = Object, { iterator: Xe, toStringTag: Pn } = Symbol, et = /* @__PURE__ */ ((e) => (t) => {
  const n = ws.call(t);
  return e[n] || (e[n] = n.slice(8, -1).toLowerCase());
})(/* @__PURE__ */ Object.create(null)), re = (e) => (e = e.toLowerCase(), (t) => et(t) === e), tt = (e) => (t) => typeof t === e, { isArray: xe } = Array, Fe = tt("undefined");
function ys(e) {
  return e !== null && !Fe(e) && e.constructor !== null && !Fe(e.constructor) && Q(e.constructor.isBuffer) && e.constructor.isBuffer(e);
}
const Nn = re("ArrayBuffer");
function vs(e) {
  let t;
  return typeof ArrayBuffer < "u" && ArrayBuffer.isView ? t = ArrayBuffer.isView(e) : t = e && e.buffer && Nn(e.buffer), t;
}
const bs = tt("string"), Q = tt("function"), Vn = tt("number"), nt = (e) => e !== null && typeof e == "object", _s = (e) => e === !0 || e === !1, Ke = (e) => {
  if (et(e) !== "object")
    return !1;
  const t = Ct(e);
  return (t === null || t === Object.prototype || Object.getPrototypeOf(t) === null) && !(Pn in e) && !(Xe in e);
}, Es = re("Date"), Rs = re("File"), xs = re("Blob"), Os = re("FileList"), Ss = (e) => nt(e) && Q(e.pipe), As = (e) => {
  let t;
  return e && (typeof FormData == "function" && e instanceof FormData || Q(e.append) && ((t = et(e)) === "formdata" || // detect form-data instance
  t === "object" && Q(e.toString) && e.toString() === "[object FormData]"));
}, Ts = re("URLSearchParams"), [ks, Cs, qs, Ps] = ["ReadableStream", "Request", "Response", "Headers"].map(re), Ns = (e) => e.trim ? e.trim() : e.replace(/^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g, "");
function Be(e, t, { allOwnKeys: n = !1 } = {}) {
  if (e === null || typeof e > "u")
    return;
  let r, s;
  if (typeof e != "object" && (e = [e]), xe(e))
    for (r = 0, s = e.length; r < s; r++)
      t.call(null, e[r], r, e);
  else {
    const o = n ? Object.getOwnPropertyNames(e) : Object.keys(e), i = o.length;
    let c;
    for (r = 0; r < i; r++)
      c = o[r], t.call(null, e[c], c, e);
  }
}
function Un(e, t) {
  t = t.toLowerCase();
  const n = Object.keys(e);
  let r = n.length, s;
  for (; r-- > 0; )
    if (s = n[r], t === s.toLowerCase())
      return s;
  return null;
}
const we = typeof globalThis < "u" ? globalThis : typeof self < "u" ? self : typeof window < "u" ? window : global, Ln = (e) => !Fe(e) && e !== we;
function Et() {
  const { caseless: e } = Ln(this) && this || {}, t = {}, n = (r, s) => {
    const o = e && Un(t, s) || s;
    Ke(t[o]) && Ke(r) ? t[o] = Et(t[o], r) : Ke(r) ? t[o] = Et({}, r) : xe(r) ? t[o] = r.slice() : t[o] = r;
  };
  for (let r = 0, s = arguments.length; r < s; r++)
    arguments[r] && Be(arguments[r], n);
  return t;
}
const Vs = (e, t, n, { allOwnKeys: r } = {}) => (Be(t, (s, o) => {
  n && Q(s) ? e[o] = qn(s, n) : e[o] = s;
}, { allOwnKeys: r }), e), Us = (e) => (e.charCodeAt(0) === 65279 && (e = e.slice(1)), e), Ls = (e, t, n, r) => {
  e.prototype = Object.create(t.prototype, r), e.prototype.constructor = e, Object.defineProperty(e, "super", {
    value: t.prototype
  }), n && Object.assign(e.prototype, n);
}, Fs = (e, t, n, r) => {
  let s, o, i;
  const c = {};
  if (t = t || {}, e == null) return t;
  do {
    for (s = Object.getOwnPropertyNames(e), o = s.length; o-- > 0; )
      i = s[o], (!r || r(i, e, t)) && !c[i] && (t[i] = e[i], c[i] = !0);
    e = n !== !1 && Ct(e);
  } while (e && (!n || n(e, t)) && e !== Object.prototype);
  return t;
}, Bs = (e, t, n) => {
  e = String(e), (n === void 0 || n > e.length) && (n = e.length), n -= t.length;
  const r = e.indexOf(t, n);
  return r !== -1 && r === n;
}, Ds = (e) => {
  if (!e) return null;
  if (xe(e)) return e;
  let t = e.length;
  if (!Vn(t)) return null;
  const n = new Array(t);
  for (; t-- > 0; )
    n[t] = e[t];
  return n;
}, Is = /* @__PURE__ */ ((e) => (t) => e && t instanceof e)(typeof Uint8Array < "u" && Ct(Uint8Array)), Ms = (e, t) => {
  const r = (e && e[Xe]).call(e);
  let s;
  for (; (s = r.next()) && !s.done; ) {
    const o = s.value;
    t.call(e, o[0], o[1]);
  }
}, $s = (e, t) => {
  let n;
  const r = [];
  for (; (n = e.exec(t)) !== null; )
    r.push(n);
  return r;
}, js = re("HTMLFormElement"), Ws = (e) => e.toLowerCase().replace(
  /[-_\s]([a-z\d])(\w*)/g,
  function(n, r, s) {
    return r.toUpperCase() + s;
  }
), en = (({ hasOwnProperty: e }) => (t, n) => e.call(t, n))(Object.prototype), zs = re("RegExp"), Fn = (e, t) => {
  const n = Object.getOwnPropertyDescriptors(e), r = {};
  Be(n, (s, o) => {
    let i;
    (i = t(s, o, e)) !== !1 && (r[o] = i || s);
  }), Object.defineProperties(e, r);
}, Hs = (e) => {
  Fn(e, (t, n) => {
    if (Q(e) && ["arguments", "caller", "callee"].indexOf(n) !== -1)
      return !1;
    const r = e[n];
    if (Q(r)) {
      if (t.enumerable = !1, "writable" in t) {
        t.writable = !1;
        return;
      }
      t.set || (t.set = () => {
        throw Error("Can not rewrite read-only method '" + n + "'");
      });
    }
  });
}, Ks = (e, t) => {
  const n = {}, r = (s) => {
    s.forEach((o) => {
      n[o] = !0;
    });
  };
  return xe(e) ? r(e) : r(String(e).split(t)), n;
}, Ys = () => {
}, Js = (e, t) => e != null && Number.isFinite(e = +e) ? e : t;
function Gs(e) {
  return !!(e && Q(e.append) && e[Pn] === "FormData" && e[Xe]);
}
const Qs = (e) => {
  const t = new Array(10), n = (r, s) => {
    if (nt(r)) {
      if (t.indexOf(r) >= 0)
        return;
      if (!("toJSON" in r)) {
        t[s] = r;
        const o = xe(r) ? [] : {};
        return Be(r, (i, c) => {
          const u = n(i, s + 1);
          !Fe(u) && (o[c] = u);
        }), t[s] = void 0, o;
      }
    }
    return r;
  };
  return n(e, 0);
}, Zs = re("AsyncFunction"), Xs = (e) => e && (nt(e) || Q(e)) && Q(e.then) && Q(e.catch), Bn = ((e, t) => e ? setImmediate : t ? ((n, r) => (we.addEventListener("message", ({ source: s, data: o }) => {
  s === we && o === n && r.length && r.shift()();
}, !1), (s) => {
  r.push(s), we.postMessage(n, "*");
}))(`axios@${Math.random()}`, []) : (n) => setTimeout(n))(
  typeof setImmediate == "function",
  Q(we.postMessage)
), eo = typeof queueMicrotask < "u" ? queueMicrotask.bind(we) : typeof process < "u" && process.nextTick || Bn, to = (e) => e != null && Q(e[Xe]), h = {
  isArray: xe,
  isArrayBuffer: Nn,
  isBuffer: ys,
  isFormData: As,
  isArrayBufferView: vs,
  isString: bs,
  isNumber: Vn,
  isBoolean: _s,
  isObject: nt,
  isPlainObject: Ke,
  isReadableStream: ks,
  isRequest: Cs,
  isResponse: qs,
  isHeaders: Ps,
  isUndefined: Fe,
  isDate: Es,
  isFile: Rs,
  isBlob: xs,
  isRegExp: zs,
  isFunction: Q,
  isStream: Ss,
  isURLSearchParams: Ts,
  isTypedArray: Is,
  isFileList: Os,
  forEach: Be,
  merge: Et,
  extend: Vs,
  trim: Ns,
  stripBOM: Us,
  inherits: Ls,
  toFlatObject: Fs,
  kindOf: et,
  kindOfTest: re,
  endsWith: Bs,
  toArray: Ds,
  forEachEntry: Ms,
  matchAll: $s,
  isHTMLForm: js,
  hasOwnProperty: en,
  hasOwnProp: en,
  // an alias to avoid ESLint no-prototype-builtins detection
  reduceDescriptors: Fn,
  freezeMethods: Hs,
  toObjectSet: Ks,
  toCamelCase: Ws,
  noop: Ys,
  toFiniteNumber: Js,
  findKey: Un,
  global: we,
  isContextDefined: Ln,
  isSpecCompliantForm: Gs,
  toJSONObject: Qs,
  isAsyncFn: Zs,
  isThenable: Xs,
  setImmediate: Bn,
  asap: eo,
  isIterable: to
};
function k(e, t, n, r, s) {
  Error.call(this), Error.captureStackTrace ? Error.captureStackTrace(this, this.constructor) : this.stack = new Error().stack, this.message = e, this.name = "AxiosError", t && (this.code = t), n && (this.config = n), r && (this.request = r), s && (this.response = s, this.status = s.status ? s.status : null);
}
h.inherits(k, Error, {
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
      status: this.status
    };
  }
});
const Dn = k.prototype, In = {};
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
  In[e] = { value: e };
});
Object.defineProperties(k, In);
Object.defineProperty(Dn, "isAxiosError", { value: !0 });
k.from = (e, t, n, r, s, o) => {
  const i = Object.create(Dn);
  return h.toFlatObject(e, i, function(u) {
    return u !== Error.prototype;
  }, (c) => c !== "isAxiosError"), k.call(i, e.message, t, n, r, s), i.cause = e, i.name = e.name, o && Object.assign(i, o), i;
};
const no = null;
function Rt(e) {
  return h.isPlainObject(e) || h.isArray(e);
}
function Mn(e) {
  return h.endsWith(e, "[]") ? e.slice(0, -2) : e;
}
function tn(e, t, n) {
  return e ? e.concat(t).map(function(s, o) {
    return s = Mn(s), !n && o ? "[" + s + "]" : s;
  }).join(n ? "." : "") : t;
}
function ro(e) {
  return h.isArray(e) && !e.some(Rt);
}
const so = h.toFlatObject(h, {}, null, function(t) {
  return /^is[A-Z]/.test(t);
});
function rt(e, t, n) {
  if (!h.isObject(e))
    throw new TypeError("target must be an object");
  t = t || new FormData(), n = h.toFlatObject(n, {
    metaTokens: !0,
    dots: !1,
    indexes: !1
  }, !1, function(b, v) {
    return !h.isUndefined(v[b]);
  });
  const r = n.metaTokens, s = n.visitor || a, o = n.dots, i = n.indexes, u = (n.Blob || typeof Blob < "u" && Blob) && h.isSpecCompliantForm(t);
  if (!h.isFunction(s))
    throw new TypeError("visitor must be a function");
  function d(g) {
    if (g === null) return "";
    if (h.isDate(g))
      return g.toISOString();
    if (h.isBoolean(g))
      return g.toString();
    if (!u && h.isBlob(g))
      throw new k("Blob is not supported. Use a Buffer instead.");
    return h.isArrayBuffer(g) || h.isTypedArray(g) ? u && typeof Blob == "function" ? new Blob([g]) : Buffer.from(g) : g;
  }
  function a(g, b, v) {
    let O = g;
    if (g && !v && typeof g == "object") {
      if (h.endsWith(b, "{}"))
        b = r ? b : b.slice(0, -2), g = JSON.stringify(g);
      else if (h.isArray(g) && ro(g) || (h.isFileList(g) || h.endsWith(b, "[]")) && (O = h.toArray(g)))
        return b = Mn(b), O.forEach(function(A, I) {
          !(h.isUndefined(A) || A === null) && t.append(
            // eslint-disable-next-line no-nested-ternary
            i === !0 ? tn([b], I, o) : i === null ? b : b + "[]",
            d(A)
          );
        }), !1;
    }
    return Rt(g) ? !0 : (t.append(tn(v, b, o), d(g)), !1);
  }
  const l = [], m = Object.assign(so, {
    defaultVisitor: a,
    convertValue: d,
    isVisitable: Rt
  });
  function w(g, b) {
    if (!h.isUndefined(g)) {
      if (l.indexOf(g) !== -1)
        throw Error("Circular reference detected in " + b.join("."));
      l.push(g), h.forEach(g, function(O, R) {
        (!(h.isUndefined(O) || O === null) && s.call(
          t,
          O,
          h.isString(R) ? R.trim() : R,
          b,
          m
        )) === !0 && w(O, b ? b.concat(R) : [R]);
      }), l.pop();
    }
  }
  if (!h.isObject(e))
    throw new TypeError("data must be an object");
  return w(e), t;
}
function nn(e) {
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
function qt(e, t) {
  this._pairs = [], e && rt(e, this, t);
}
const $n = qt.prototype;
$n.append = function(t, n) {
  this._pairs.push([t, n]);
};
$n.toString = function(t) {
  const n = t ? function(r) {
    return t.call(this, r, nn);
  } : nn;
  return this._pairs.map(function(s) {
    return n(s[0]) + "=" + n(s[1]);
  }, "").join("&");
};
function oo(e) {
  return encodeURIComponent(e).replace(/%3A/gi, ":").replace(/%24/g, "$").replace(/%2C/gi, ",").replace(/%20/g, "+").replace(/%5B/gi, "[").replace(/%5D/gi, "]");
}
function jn(e, t, n) {
  if (!t)
    return e;
  const r = n && n.encode || oo;
  h.isFunction(n) && (n = {
    serialize: n
  });
  const s = n && n.serialize;
  let o;
  if (s ? o = s(t, n) : o = h.isURLSearchParams(t) ? t.toString() : new qt(t, n).toString(r), o) {
    const i = e.indexOf("#");
    i !== -1 && (e = e.slice(0, i)), e += (e.indexOf("?") === -1 ? "?" : "&") + o;
  }
  return e;
}
class rn {
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
const Wn = {
  silentJSONParsing: !0,
  forcedJSONParsing: !0,
  clarifyTimeoutError: !1
}, io = typeof URLSearchParams < "u" ? URLSearchParams : qt, ao = typeof FormData < "u" ? FormData : null, lo = typeof Blob < "u" ? Blob : null, co = {
  isBrowser: !0,
  classes: {
    URLSearchParams: io,
    FormData: ao,
    Blob: lo
  },
  protocols: ["http", "https", "file", "blob", "url", "data"]
}, Pt = typeof window < "u" && typeof document < "u", xt = typeof navigator == "object" && navigator || void 0, uo = Pt && (!xt || ["ReactNative", "NativeScript", "NS"].indexOf(xt.product) < 0), fo = typeof WorkerGlobalScope < "u" && // eslint-disable-next-line no-undef
self instanceof WorkerGlobalScope && typeof self.importScripts == "function", ho = Pt && window.location.href || "http://localhost", po = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  hasBrowserEnv: Pt,
  hasStandardBrowserEnv: uo,
  hasStandardBrowserWebWorkerEnv: fo,
  navigator: xt,
  origin: ho
}, Symbol.toStringTag, { value: "Module" })), J = {
  ...po,
  ...co
};
function mo(e, t) {
  return rt(e, new J.classes.URLSearchParams(), Object.assign({
    visitor: function(n, r, s, o) {
      return J.isNode && h.isBuffer(n) ? (this.append(r, n.toString("base64")), !1) : o.defaultVisitor.apply(this, arguments);
    }
  }, t));
}
function go(e) {
  return h.matchAll(/\w+|\[(\w*)]/g, e).map((t) => t[0] === "[]" ? "" : t[1] || t[0]);
}
function wo(e) {
  const t = {}, n = Object.keys(e);
  let r;
  const s = n.length;
  let o;
  for (r = 0; r < s; r++)
    o = n[r], t[o] = e[o];
  return t;
}
function zn(e) {
  function t(n, r, s, o) {
    let i = n[o++];
    if (i === "__proto__") return !0;
    const c = Number.isFinite(+i), u = o >= n.length;
    return i = !i && h.isArray(s) ? s.length : i, u ? (h.hasOwnProp(s, i) ? s[i] = [s[i], r] : s[i] = r, !c) : ((!s[i] || !h.isObject(s[i])) && (s[i] = []), t(n, r, s[i], o) && h.isArray(s[i]) && (s[i] = wo(s[i])), !c);
  }
  if (h.isFormData(e) && h.isFunction(e.entries)) {
    const n = {};
    return h.forEachEntry(e, (r, s) => {
      t(go(r), s, n, 0);
    }), n;
  }
  return null;
}
function yo(e, t, n) {
  if (h.isString(e))
    try {
      return (t || JSON.parse)(e), h.trim(e);
    } catch (r) {
      if (r.name !== "SyntaxError")
        throw r;
    }
  return (n || JSON.stringify)(e);
}
const De = {
  transitional: Wn,
  adapter: ["xhr", "http", "fetch"],
  transformRequest: [function(t, n) {
    const r = n.getContentType() || "", s = r.indexOf("application/json") > -1, o = h.isObject(t);
    if (o && h.isHTMLForm(t) && (t = new FormData(t)), h.isFormData(t))
      return s ? JSON.stringify(zn(t)) : t;
    if (h.isArrayBuffer(t) || h.isBuffer(t) || h.isStream(t) || h.isFile(t) || h.isBlob(t) || h.isReadableStream(t))
      return t;
    if (h.isArrayBufferView(t))
      return t.buffer;
    if (h.isURLSearchParams(t))
      return n.setContentType("application/x-www-form-urlencoded;charset=utf-8", !1), t.toString();
    let c;
    if (o) {
      if (r.indexOf("application/x-www-form-urlencoded") > -1)
        return mo(t, this.formSerializer).toString();
      if ((c = h.isFileList(t)) || r.indexOf("multipart/form-data") > -1) {
        const u = this.env && this.env.FormData;
        return rt(
          c ? { "files[]": t } : t,
          u && new u(),
          this.formSerializer
        );
      }
    }
    return o || s ? (n.setContentType("application/json", !1), yo(t)) : t;
  }],
  transformResponse: [function(t) {
    const n = this.transitional || De.transitional, r = n && n.forcedJSONParsing, s = this.responseType === "json";
    if (h.isResponse(t) || h.isReadableStream(t))
      return t;
    if (t && h.isString(t) && (r && !this.responseType || s)) {
      const i = !(n && n.silentJSONParsing) && s;
      try {
        return JSON.parse(t);
      } catch (c) {
        if (i)
          throw c.name === "SyntaxError" ? k.from(c, k.ERR_BAD_RESPONSE, this, null, this.response) : c;
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
    FormData: J.classes.FormData,
    Blob: J.classes.Blob
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
  De.headers[e] = {};
});
const vo = h.toObjectSet([
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
]), bo = (e) => {
  const t = {};
  let n, r, s;
  return e && e.split(`
`).forEach(function(i) {
    s = i.indexOf(":"), n = i.substring(0, s).trim().toLowerCase(), r = i.substring(s + 1).trim(), !(!n || t[n] && vo[n]) && (n === "set-cookie" ? t[n] ? t[n].push(r) : t[n] = [r] : t[n] = t[n] ? t[n] + ", " + r : r);
  }), t;
}, sn = Symbol("internals");
function Te(e) {
  return e && String(e).trim().toLowerCase();
}
function Ye(e) {
  return e === !1 || e == null ? e : h.isArray(e) ? e.map(Ye) : String(e);
}
function _o(e) {
  const t = /* @__PURE__ */ Object.create(null), n = /([^\s,;=]+)\s*(?:=\s*([^,;]+))?/g;
  let r;
  for (; r = n.exec(e); )
    t[r[1]] = r[2];
  return t;
}
const Eo = (e) => /^[-_a-zA-Z0-9^`|~,!#$%&'*+.]+$/.test(e.trim());
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
function Ro(e) {
  return e.trim().toLowerCase().replace(/([a-z\d])(\w*)/g, (t, n, r) => n.toUpperCase() + r);
}
function xo(e, t) {
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
let Z = class {
  constructor(t) {
    t && this.set(t);
  }
  set(t, n, r) {
    const s = this;
    function o(c, u, d) {
      const a = Te(u);
      if (!a)
        throw new Error("header name must be a non-empty string");
      const l = h.findKey(s, a);
      (!l || s[l] === void 0 || d === !0 || d === void 0 && s[l] !== !1) && (s[l || u] = Ye(c));
    }
    const i = (c, u) => h.forEach(c, (d, a) => o(d, a, u));
    if (h.isPlainObject(t) || t instanceof this.constructor)
      i(t, n);
    else if (h.isString(t) && (t = t.trim()) && !Eo(t))
      i(bo(t), n);
    else if (h.isObject(t) && h.isIterable(t)) {
      let c = {}, u, d;
      for (const a of t) {
        if (!h.isArray(a))
          throw TypeError("Object iterator must return a key-value pair");
        c[d = a[0]] = (u = c[d]) ? h.isArray(u) ? [...u, a[1]] : [u, a[1]] : a[1];
      }
      i(c, n);
    } else
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
          return _o(s);
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
        const c = h.findKey(r, i);
        c && (!n || mt(r, r[c], c, n)) && (delete r[c], s = !0);
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
        n[i] = Ye(s), delete n[o];
        return;
      }
      const c = t ? Ro(o) : String(o).trim();
      c !== o && delete n[o], n[c] = Ye(s), r[c] = !0;
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
  getSetCookie() {
    return this.get("set-cookie") || [];
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
    const r = (this[sn] = this[sn] = {
      accessors: {}
    }).accessors, s = this.prototype;
    function o(i) {
      const c = Te(i);
      r[c] || (xo(s, i), r[c] = !0);
    }
    return h.isArray(t) ? t.forEach(o) : o(t), this;
  }
};
Z.accessor(["Content-Type", "Content-Length", "Accept", "Accept-Encoding", "User-Agent", "Authorization"]);
h.reduceDescriptors(Z.prototype, ({ value: e }, t) => {
  let n = t[0].toUpperCase() + t.slice(1);
  return {
    get: () => e,
    set(r) {
      this[n] = r;
    }
  };
});
h.freezeMethods(Z);
function gt(e, t) {
  const n = this || De, r = t || n, s = Z.from(r.headers);
  let o = r.data;
  return h.forEach(e, function(c) {
    o = c.call(n, o, s.normalize(), t ? t.status : void 0);
  }), s.normalize(), o;
}
function Hn(e) {
  return !!(e && e.__CANCEL__);
}
function Oe(e, t, n) {
  k.call(this, e ?? "canceled", k.ERR_CANCELED, t, n), this.name = "CanceledError";
}
h.inherits(Oe, k, {
  __CANCEL__: !0
});
function Kn(e, t, n) {
  const r = n.config.validateStatus;
  !n.status || !r || r(n.status) ? e(n) : t(new k(
    "Request failed with status code " + n.status,
    [k.ERR_BAD_REQUEST, k.ERR_BAD_RESPONSE][Math.floor(n.status / 100) - 4],
    n.config,
    n.request,
    n
  ));
}
function Oo(e) {
  const t = /^([-+\w]{1,25})(:?\/\/|:)/.exec(e);
  return t && t[1] || "";
}
function So(e, t) {
  e = e || 10;
  const n = new Array(e), r = new Array(e);
  let s = 0, o = 0, i;
  return t = t !== void 0 ? t : 1e3, function(u) {
    const d = Date.now(), a = r[o];
    i || (i = d), n[s] = u, r[s] = d;
    let l = o, m = 0;
    for (; l !== s; )
      m += n[l++], l = l % e;
    if (s = (s + 1) % e, s === o && (o = (o + 1) % e), d - i < t)
      return;
    const w = a && d - a;
    return w ? Math.round(m * 1e3 / w) : void 0;
  };
}
function Ao(e, t) {
  let n = 0, r = 1e3 / t, s, o;
  const i = (d, a = Date.now()) => {
    n = a, s = null, o && (clearTimeout(o), o = null), e.apply(null, d);
  };
  return [(...d) => {
    const a = Date.now(), l = a - n;
    l >= r ? i(d, a) : (s = d, o || (o = setTimeout(() => {
      o = null, i(s);
    }, r - l)));
  }, () => s && i(s)];
}
const Ge = (e, t, n = 3) => {
  let r = 0;
  const s = So(50, 250);
  return Ao((o) => {
    const i = o.loaded, c = o.lengthComputable ? o.total : void 0, u = i - r, d = s(u), a = i <= c;
    r = i;
    const l = {
      loaded: i,
      total: c,
      progress: c ? i / c : void 0,
      bytes: u,
      rate: d || void 0,
      estimated: d && c && a ? (c - i) / d : void 0,
      event: o,
      lengthComputable: c != null,
      [t ? "download" : "upload"]: !0
    };
    e(l);
  }, n);
}, on = (e, t) => {
  const n = e != null;
  return [(r) => t[0]({
    lengthComputable: n,
    total: e,
    loaded: r
  }), t[1]];
}, an = (e) => (...t) => h.asap(() => e(...t)), To = J.hasStandardBrowserEnv ? /* @__PURE__ */ ((e, t) => (n) => (n = new URL(n, J.origin), e.protocol === n.protocol && e.host === n.host && (t || e.port === n.port)))(
  new URL(J.origin),
  J.navigator && /(msie|trident)/i.test(J.navigator.userAgent)
) : () => !0, ko = J.hasStandardBrowserEnv ? (
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
function qo(e, t) {
  return t ? e.replace(/\/?\/$/, "") + "/" + t.replace(/^\/+/, "") : e;
}
function Yn(e, t, n) {
  let r = !Co(t);
  return e && (r || n == !1) ? qo(e, t) : t;
}
const ln = (e) => e instanceof Z ? { ...e } : e;
function ve(e, t) {
  t = t || {};
  const n = {};
  function r(d, a, l, m) {
    return h.isPlainObject(d) && h.isPlainObject(a) ? h.merge.call({ caseless: m }, d, a) : h.isPlainObject(a) ? h.merge({}, a) : h.isArray(a) ? a.slice() : a;
  }
  function s(d, a, l, m) {
    if (h.isUndefined(a)) {
      if (!h.isUndefined(d))
        return r(void 0, d, l, m);
    } else return r(d, a, l, m);
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
  function c(d, a, l) {
    if (l in t)
      return r(d, a);
    if (l in e)
      return r(void 0, d);
  }
  const u = {
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
    validateStatus: c,
    headers: (d, a, l) => s(ln(d), ln(a), l, !0)
  };
  return h.forEach(Object.keys(Object.assign({}, e, t)), function(a) {
    const l = u[a] || s, m = l(e[a], t[a], a);
    h.isUndefined(m) && l !== c || (n[a] = m);
  }), n;
}
const Jn = (e) => {
  const t = ve({}, e);
  let { data: n, withXSRFToken: r, xsrfHeaderName: s, xsrfCookieName: o, headers: i, auth: c } = t;
  t.headers = i = Z.from(i), t.url = jn(Yn(t.baseURL, t.url, t.allowAbsoluteUrls), e.params, e.paramsSerializer), c && i.set(
    "Authorization",
    "Basic " + btoa((c.username || "") + ":" + (c.password ? unescape(encodeURIComponent(c.password)) : ""))
  );
  let u;
  if (h.isFormData(n)) {
    if (J.hasStandardBrowserEnv || J.hasStandardBrowserWebWorkerEnv)
      i.setContentType(void 0);
    else if ((u = i.getContentType()) !== !1) {
      const [d, ...a] = u ? u.split(";").map((l) => l.trim()).filter(Boolean) : [];
      i.setContentType([d || "multipart/form-data", ...a].join("; "));
    }
  }
  if (J.hasStandardBrowserEnv && (r && h.isFunction(r) && (r = r(t)), r || r !== !1 && To(t.url))) {
    const d = s && o && ko.read(o);
    d && i.set(s, d);
  }
  return t;
}, Po = typeof XMLHttpRequest < "u", No = Po && function(e) {
  return new Promise(function(n, r) {
    const s = Jn(e);
    let o = s.data;
    const i = Z.from(s.headers).normalize();
    let { responseType: c, onUploadProgress: u, onDownloadProgress: d } = s, a, l, m, w, g;
    function b() {
      w && w(), g && g(), s.cancelToken && s.cancelToken.unsubscribe(a), s.signal && s.signal.removeEventListener("abort", a);
    }
    let v = new XMLHttpRequest();
    v.open(s.method.toUpperCase(), s.url, !0), v.timeout = s.timeout;
    function O() {
      if (!v)
        return;
      const A = Z.from(
        "getAllResponseHeaders" in v && v.getAllResponseHeaders()
      ), B = {
        data: !c || c === "text" || c === "json" ? v.responseText : v.response,
        status: v.status,
        statusText: v.statusText,
        headers: A,
        config: e,
        request: v
      };
      Kn(function(M) {
        n(M), b();
      }, function(M) {
        r(M), b();
      }, B), v = null;
    }
    "onloadend" in v ? v.onloadend = O : v.onreadystatechange = function() {
      !v || v.readyState !== 4 || v.status === 0 && !(v.responseURL && v.responseURL.indexOf("file:") === 0) || setTimeout(O);
    }, v.onabort = function() {
      v && (r(new k("Request aborted", k.ECONNABORTED, e, v)), v = null);
    }, v.onerror = function() {
      r(new k("Network Error", k.ERR_NETWORK, e, v)), v = null;
    }, v.ontimeout = function() {
      let I = s.timeout ? "timeout of " + s.timeout + "ms exceeded" : "timeout exceeded";
      const B = s.transitional || Wn;
      s.timeoutErrorMessage && (I = s.timeoutErrorMessage), r(new k(
        I,
        B.clarifyTimeoutError ? k.ETIMEDOUT : k.ECONNABORTED,
        e,
        v
      )), v = null;
    }, o === void 0 && i.setContentType(null), "setRequestHeader" in v && h.forEach(i.toJSON(), function(I, B) {
      v.setRequestHeader(B, I);
    }), h.isUndefined(s.withCredentials) || (v.withCredentials = !!s.withCredentials), c && c !== "json" && (v.responseType = s.responseType), d && ([m, g] = Ge(d, !0), v.addEventListener("progress", m)), u && v.upload && ([l, w] = Ge(u), v.upload.addEventListener("progress", l), v.upload.addEventListener("loadend", w)), (s.cancelToken || s.signal) && (a = (A) => {
      v && (r(!A || A.type ? new Oe(null, e, v) : A), v.abort(), v = null);
    }, s.cancelToken && s.cancelToken.subscribe(a), s.signal && (s.signal.aborted ? a() : s.signal.addEventListener("abort", a)));
    const R = Oo(s.url);
    if (R && J.protocols.indexOf(R) === -1) {
      r(new k("Unsupported protocol " + R + ":", k.ERR_BAD_REQUEST, e));
      return;
    }
    v.send(o || null);
  });
}, Vo = (e, t) => {
  const { length: n } = e = e ? e.filter(Boolean) : [];
  if (t || n) {
    let r = new AbortController(), s;
    const o = function(d) {
      if (!s) {
        s = !0, c();
        const a = d instanceof Error ? d : this.reason;
        r.abort(a instanceof k ? a : new Oe(a instanceof Error ? a.message : a));
      }
    };
    let i = t && setTimeout(() => {
      i = null, o(new k(`timeout ${t} of ms exceeded`, k.ETIMEDOUT));
    }, t);
    const c = () => {
      e && (i && clearTimeout(i), i = null, e.forEach((d) => {
        d.unsubscribe ? d.unsubscribe(o) : d.removeEventListener("abort", o);
      }), e = null);
    };
    e.forEach((d) => d.addEventListener("abort", o));
    const { signal: u } = r;
    return u.unsubscribe = () => h.asap(c), u;
  }
}, Uo = function* (e, t) {
  let n = e.byteLength;
  if (n < t) {
    yield e;
    return;
  }
  let r = 0, s;
  for (; r < n; )
    s = r + t, yield e.slice(r, s), r = s;
}, Lo = async function* (e, t) {
  for await (const n of Fo(e))
    yield* Uo(n, t);
}, Fo = async function* (e) {
  if (e[Symbol.asyncIterator]) {
    yield* e;
    return;
  }
  const t = e.getReader();
  try {
    for (; ; ) {
      const { done: n, value: r } = await t.read();
      if (n)
        break;
      yield r;
    }
  } finally {
    await t.cancel();
  }
}, cn = (e, t, n, r) => {
  const s = Lo(e, t);
  let o = 0, i, c = (u) => {
    i || (i = !0, r && r(u));
  };
  return new ReadableStream({
    async pull(u) {
      try {
        const { done: d, value: a } = await s.next();
        if (d) {
          c(), u.close();
          return;
        }
        let l = a.byteLength;
        if (n) {
          let m = o += l;
          n(m);
        }
        u.enqueue(new Uint8Array(a));
      } catch (d) {
        throw c(d), d;
      }
    },
    cancel(u) {
      return c(u), s.return();
    }
  }, {
    highWaterMark: 2
  });
}, st = typeof fetch == "function" && typeof Request == "function" && typeof Response == "function", Gn = st && typeof ReadableStream == "function", Bo = st && (typeof TextEncoder == "function" ? /* @__PURE__ */ ((e) => (t) => e.encode(t))(new TextEncoder()) : async (e) => new Uint8Array(await new Response(e).arrayBuffer())), Qn = (e, ...t) => {
  try {
    return !!e(...t);
  } catch {
    return !1;
  }
}, Do = Gn && Qn(() => {
  let e = !1;
  const t = new Request(J.origin, {
    body: new ReadableStream(),
    method: "POST",
    get duplex() {
      return e = !0, "half";
    }
  }).headers.has("Content-Type");
  return e && !t;
}), un = 64 * 1024, Ot = Gn && Qn(() => h.isReadableStream(new Response("").body)), Qe = {
  stream: Ot && ((e) => e.body)
};
st && ((e) => {
  ["text", "arrayBuffer", "blob", "formData", "stream"].forEach((t) => {
    !Qe[t] && (Qe[t] = h.isFunction(e[t]) ? (n) => n[t]() : (n, r) => {
      throw new k(`Response type '${t}' is not supported`, k.ERR_NOT_SUPPORT, r);
    });
  });
})(new Response());
const Io = async (e) => {
  if (e == null)
    return 0;
  if (h.isBlob(e))
    return e.size;
  if (h.isSpecCompliantForm(e))
    return (await new Request(J.origin, {
      method: "POST",
      body: e
    }).arrayBuffer()).byteLength;
  if (h.isArrayBufferView(e) || h.isArrayBuffer(e))
    return e.byteLength;
  if (h.isURLSearchParams(e) && (e = e + ""), h.isString(e))
    return (await Bo(e)).byteLength;
}, Mo = async (e, t) => {
  const n = h.toFiniteNumber(e.getContentLength());
  return n ?? Io(t);
}, $o = st && (async (e) => {
  let {
    url: t,
    method: n,
    data: r,
    signal: s,
    cancelToken: o,
    timeout: i,
    onDownloadProgress: c,
    onUploadProgress: u,
    responseType: d,
    headers: a,
    withCredentials: l = "same-origin",
    fetchOptions: m
  } = Jn(e);
  d = d ? (d + "").toLowerCase() : "text";
  let w = Vo([s, o && o.toAbortSignal()], i), g;
  const b = w && w.unsubscribe && (() => {
    w.unsubscribe();
  });
  let v;
  try {
    if (u && Do && n !== "get" && n !== "head" && (v = await Mo(a, r)) !== 0) {
      let B = new Request(t, {
        method: "POST",
        body: r,
        duplex: "half"
      }), j;
      if (h.isFormData(r) && (j = B.headers.get("content-type")) && a.setContentType(j), B.body) {
        const [M, fe] = on(
          v,
          Ge(an(u))
        );
        r = cn(B.body, un, M, fe);
      }
    }
    h.isString(l) || (l = l ? "include" : "omit");
    const O = "credentials" in Request.prototype;
    g = new Request(t, {
      ...m,
      signal: w,
      method: n.toUpperCase(),
      headers: a.normalize().toJSON(),
      body: r,
      duplex: "half",
      credentials: O ? l : void 0
    });
    let R = await fetch(g, m);
    const A = Ot && (d === "stream" || d === "response");
    if (Ot && (c || A && b)) {
      const B = {};
      ["status", "statusText", "headers"].forEach((le) => {
        B[le] = R[le];
      });
      const j = h.toFiniteNumber(R.headers.get("content-length")), [M, fe] = c && on(
        j,
        Ge(an(c), !0)
      ) || [];
      R = new Response(
        cn(R.body, un, M, () => {
          fe && fe(), b && b();
        }),
        B
      );
    }
    d = d || "text";
    let I = await Qe[h.findKey(Qe, d) || "text"](R, e);
    return !A && b && b(), await new Promise((B, j) => {
      Kn(B, j, {
        data: I,
        headers: Z.from(R.headers),
        status: R.status,
        statusText: R.statusText,
        config: e,
        request: g
      });
    });
  } catch (O) {
    throw b && b(), O && O.name === "TypeError" && /Load failed|fetch/i.test(O.message) ? Object.assign(
      new k("Network Error", k.ERR_NETWORK, e, g),
      {
        cause: O.cause || O
      }
    ) : k.from(O, O && O.code, e, g);
  }
}), St = {
  http: no,
  xhr: No,
  fetch: $o
};
h.forEach(St, (e, t) => {
  if (e) {
    try {
      Object.defineProperty(e, "name", { value: t });
    } catch {
    }
    Object.defineProperty(e, "adapterName", { value: t });
  }
});
const dn = (e) => `- ${e}`, jo = (e) => h.isFunction(e) || e === null || e === !1, Zn = {
  getAdapter: (e) => {
    e = h.isArray(e) ? e : [e];
    const { length: t } = e;
    let n, r;
    const s = {};
    for (let o = 0; o < t; o++) {
      n = e[o];
      let i;
      if (r = n, !jo(n) && (r = St[(i = String(n)).toLowerCase()], r === void 0))
        throw new k(`Unknown adapter '${i}'`);
      if (r)
        break;
      s[i || "#" + o] = r;
    }
    if (!r) {
      const o = Object.entries(s).map(
        ([c, u]) => `adapter ${c} ` + (u === !1 ? "is not supported by the environment" : "is not available in the build")
      );
      let i = t ? o.length > 1 ? `since :
` + o.map(dn).join(`
`) : " " + dn(o[0]) : "as no adapter specified";
      throw new k(
        "There is no suitable adapter to dispatch the request " + i,
        "ERR_NOT_SUPPORT"
      );
    }
    return r;
  },
  adapters: St
};
function wt(e) {
  if (e.cancelToken && e.cancelToken.throwIfRequested(), e.signal && e.signal.aborted)
    throw new Oe(null, e);
}
function fn(e) {
  return wt(e), e.headers = Z.from(e.headers), e.data = gt.call(
    e,
    e.transformRequest
  ), ["post", "put", "patch"].indexOf(e.method) !== -1 && e.headers.setContentType("application/x-www-form-urlencoded", !1), Zn.getAdapter(e.adapter || De.adapter)(e).then(function(r) {
    return wt(e), r.data = gt.call(
      e,
      e.transformResponse,
      r
    ), r.headers = Z.from(r.headers), r;
  }, function(r) {
    return Hn(r) || (wt(e), r && r.response && (r.response.data = gt.call(
      e,
      e.transformResponse,
      r.response
    ), r.response.headers = Z.from(r.response.headers))), Promise.reject(r);
  });
}
const Xn = "1.10.0", ot = {};
["object", "boolean", "number", "function", "string", "symbol"].forEach((e, t) => {
  ot[e] = function(r) {
    return typeof r === e || "a" + (t < 1 ? "n " : " ") + e;
  };
});
const hn = {};
ot.transitional = function(t, n, r) {
  function s(o, i) {
    return "[Axios v" + Xn + "] Transitional option '" + o + "'" + i + (r ? ". " + r : "");
  }
  return (o, i, c) => {
    if (t === !1)
      throw new k(
        s(i, " has been removed" + (n ? " in " + n : "")),
        k.ERR_DEPRECATED
      );
    return n && !hn[i] && (hn[i] = !0, console.warn(
      s(
        i,
        " has been deprecated since v" + n + " and will be removed in the near future"
      )
    )), t ? t(o, i, c) : !0;
  };
};
ot.spelling = function(t) {
  return (n, r) => (console.warn(`${r} is likely a misspelling of ${t}`), !0);
};
function Wo(e, t, n) {
  if (typeof e != "object")
    throw new k("options must be an object", k.ERR_BAD_OPTION_VALUE);
  const r = Object.keys(e);
  let s = r.length;
  for (; s-- > 0; ) {
    const o = r[s], i = t[o];
    if (i) {
      const c = e[o], u = c === void 0 || i(c, o, e);
      if (u !== !0)
        throw new k("option " + o + " must be " + u, k.ERR_BAD_OPTION_VALUE);
      continue;
    }
    if (n !== !0)
      throw new k("Unknown option " + o, k.ERR_BAD_OPTION);
  }
}
const Je = {
  assertOptions: Wo,
  validators: ot
}, oe = Je.validators;
let ye = class {
  constructor(t) {
    this.defaults = t || {}, this.interceptors = {
      request: new rn(),
      response: new rn()
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
        let s = {};
        Error.captureStackTrace ? Error.captureStackTrace(s) : s = new Error();
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
    typeof t == "string" ? (n = n || {}, n.url = t) : n = t || {}, n = ve(this.defaults, n);
    const { transitional: r, paramsSerializer: s, headers: o } = n;
    r !== void 0 && Je.assertOptions(r, {
      silentJSONParsing: oe.transitional(oe.boolean),
      forcedJSONParsing: oe.transitional(oe.boolean),
      clarifyTimeoutError: oe.transitional(oe.boolean)
    }, !1), s != null && (h.isFunction(s) ? n.paramsSerializer = {
      serialize: s
    } : Je.assertOptions(s, {
      encode: oe.function,
      serialize: oe.function
    }, !0)), n.allowAbsoluteUrls !== void 0 || (this.defaults.allowAbsoluteUrls !== void 0 ? n.allowAbsoluteUrls = this.defaults.allowAbsoluteUrls : n.allowAbsoluteUrls = !0), Je.assertOptions(n, {
      baseUrl: oe.spelling("baseURL"),
      withXsrfToken: oe.spelling("withXSRFToken")
    }, !0), n.method = (n.method || this.defaults.method || "get").toLowerCase();
    let i = o && h.merge(
      o.common,
      o[n.method]
    );
    o && h.forEach(
      ["delete", "get", "head", "post", "put", "patch", "common"],
      (g) => {
        delete o[g];
      }
    ), n.headers = Z.concat(i, o);
    const c = [];
    let u = !0;
    this.interceptors.request.forEach(function(b) {
      typeof b.runWhen == "function" && b.runWhen(n) === !1 || (u = u && b.synchronous, c.unshift(b.fulfilled, b.rejected));
    });
    const d = [];
    this.interceptors.response.forEach(function(b) {
      d.push(b.fulfilled, b.rejected);
    });
    let a, l = 0, m;
    if (!u) {
      const g = [fn.bind(this), void 0];
      for (g.unshift.apply(g, c), g.push.apply(g, d), m = g.length, a = Promise.resolve(n); l < m; )
        a = a.then(g[l++], g[l++]);
      return a;
    }
    m = c.length;
    let w = n;
    for (l = 0; l < m; ) {
      const g = c[l++], b = c[l++];
      try {
        w = g(w);
      } catch (v) {
        b.call(this, v);
        break;
      }
    }
    try {
      a = fn.call(this, w);
    } catch (g) {
      return Promise.reject(g);
    }
    for (l = 0, m = d.length; l < m; )
      a = a.then(d[l++], d[l++]);
    return a;
  }
  getUri(t) {
    t = ve(this.defaults, t);
    const n = Yn(t.baseURL, t.url, t.allowAbsoluteUrls);
    return jn(n, t.params, t.paramsSerializer);
  }
};
h.forEach(["delete", "get", "head", "options"], function(t) {
  ye.prototype[t] = function(n, r) {
    return this.request(ve(r || {}, {
      method: t,
      url: n,
      data: (r || {}).data
    }));
  };
});
h.forEach(["post", "put", "patch"], function(t) {
  function n(r) {
    return function(o, i, c) {
      return this.request(ve(c || {}, {
        method: t,
        headers: r ? {
          "Content-Type": "multipart/form-data"
        } : {},
        url: o,
        data: i
      }));
    };
  }
  ye.prototype[t] = n(), ye.prototype[t + "Form"] = n(!0);
});
let zo = class er {
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
      const i = new Promise((c) => {
        r.subscribe(c), o = c;
      }).then(s);
      return i.cancel = function() {
        r.unsubscribe(o);
      }, i;
    }, t(function(o, i, c) {
      r.reason || (r.reason = new Oe(o, i, c), n(r.reason));
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
  toAbortSignal() {
    const t = new AbortController(), n = (r) => {
      t.abort(r);
    };
    return this.subscribe(n), t.signal.unsubscribe = () => this.unsubscribe(n), t.signal;
  }
  /**
   * Returns an object that contains a new `CancelToken` and a function that, when called,
   * cancels the `CancelToken`.
   */
  static source() {
    let t;
    return {
      token: new er(function(s) {
        t = s;
      }),
      cancel: t
    };
  }
};
function Ho(e) {
  return function(n) {
    return e.apply(null, n);
  };
}
function Ko(e) {
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
  const t = new ye(e), n = qn(ye.prototype.request, t);
  return h.extend(n, ye.prototype, t, { allOwnKeys: !0 }), h.extend(n, t, null, { allOwnKeys: !0 }), n.create = function(s) {
    return tr(ve(e, s));
  }, n;
}
const L = tr(De);
L.Axios = ye;
L.CanceledError = Oe;
L.CancelToken = zo;
L.isCancel = Hn;
L.VERSION = Xn;
L.toFormData = rt;
L.AxiosError = k;
L.Cancel = L.CanceledError;
L.all = function(t) {
  return Promise.all(t);
};
L.spread = Ho;
L.isAxiosError = Ko;
L.mergeConfig = ve;
L.AxiosHeaders = Z;
L.formToJSON = (e) => zn(h.isHTMLForm(e) ? new FormData(e) : e);
L.getAdapter = Zn.getAdapter;
L.HttpStatusCode = At;
L.default = L;
const {
  Axios: Aa,
  AxiosError: Ta,
  CanceledError: ka,
  isCancel: Ca,
  CancelToken: qa,
  VERSION: Pa,
  all: Na,
  Cancel: Va,
  isAxiosError: Ua,
  spread: La,
  toFormData: Fa,
  AxiosHeaders: Ba,
  HttpStatusCode: Da,
  formToJSON: Ia,
  getAdapter: Ma,
  mergeConfig: $a
} = L, Ie = (e, t) => {
  const n = e.__vccOpts || e;
  for (const [r, s] of t)
    n[r] = s;
  return n;
}, Yo = {
  created() {
    this.getWeek(0);
  },
  methods: {
    formatDate(e) {
      let t = window.Quasar.date.extractDate(
        e.substring(0, 16),
        "YYYY-MM-DDTHH:mm"
      );
      return window.Quasar.date.formatDate(t, "DD/MM/YYYY");
    },
    updateData() {
      this.loading = !0, this.fail = !1, this.data = [], L.get(
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
      let t = window.Quasar.date.addToDate(Date.now(), {
        days: e * 7
      }), n = window.Quasar.date.getDayOfWeek(Date.now()), r = window.Quasar.date.subtractFromDate(t, {
        days: n ? n - 1 : 6
      }), s = window.Quasar.date.addToDate(t, {
        days: n ? 7 - n : 0
      });
      return {
        startOfWeek: window.Quasar.date.formatDate(r, "DD/MM/YYYY"),
        endOfWeek: window.Quasar.date.formatDate(s, "DD/MM/YYYY")
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
      //Status must not be empty nor in lowercase letters. Any uppercase string would do the job as long as it is different than the keywords: SUCCESS, ERROR et MISFIRED
      tab: "all",
      startOfWeek: "",
      endOfWeek: ""
    };
  },
  watch: {
    "$q.lang": function() {
      this.columns = this.columns.map((e) => ({ ...e, label: this.$vui.i18n().vuiOrchestra.orchestra[e.name] }));
    }
  }
}, X = window.Vue.toDisplayString, Y = window.Vue.createElementVNode, pe = window.Vue.resolveComponent, K = window.Vue.createVNode, me = window.Vue.withCtx, We = window.Vue.createTextVNode, ze = window.Vue.openBlock, He = window.Vue.createElementBlock;
window.Vue.createCommentVNode;
const Jo = { class: "row justify-center" }, Go = { style: { width: "1300px" } }, Qo = { class: "text-h5 row q-mt-lg" }, Zo = { class: "q-mx-auto" }, Xo = { class: "row q-mt-lg" }, ei = { class: "row q-mt-lg" }, ti = { class: "q-mx-auto q-gutter-md" }, ni = { class: "q-pa-md" }, ri = { class: "row q-col-gutter-x-md" }, si = { class: "col text-center" }, oi = { class: "col text-center" }, ii = { class: "col text-center" }, ai = { class: "row q-col-gutter-x-md" }, li = { class: "col text-center" }, ci = { class: "col text-center" }, ui = { class: "col text-center" }, di = { class: "full-width row flex-center q-gutter-sm" }, fi = { key: 0 }, hi = { key: 1 }, pi = { key: 2 };
function mi(e, t, n, r, s, o) {
  const i = pe("q-tab"), c = pe("q-tabs"), u = pe("q-btn"), d = pe("q-td"), a = pe("q-icon"), l = pe("q-tr"), m = pe("q-spinner"), w = pe("q-table");
  return ze(), He("div", Jo, [
    Y("div", Go, [
      Y("div", Qo, [
        Y("div", Zo, X(e.$vui.i18n().vuiOrchestra.orchestra.title.part1) + " " + X(s.startOfWeek) + " " + X(e.$vui.i18n().vuiOrchestra.orchestra.title.part2) + " " + X(s.endOfWeek), 1)
      ]),
      Y("div", Xo, [
        K(c, {
          modelValue: s.tab,
          "onUpdate:modelValue": t[4] || (t[4] = (g) => s.tab = g),
          "inline-label": "",
          class: "text-primary q-mx-auto"
        }, {
          default: me(() => [
            K(i, {
              name: "all",
              icon: "list",
              label: e.$vui.i18n().vuiOrchestra.orchestra.all,
              onClick: t[0] || (t[0] = (g) => o.getStatus("A"))
            }, null, 8, ["label"]),
            K(i, {
              name: "success",
              icon: "done",
              label: e.$vui.i18n().vuiOrchestra.orchestra.success,
              onClick: t[1] || (t[1] = (g) => o.getStatus("SUCCESS")),
              class: "text-green"
            }, null, 8, ["label"]),
            K(i, {
              name: "error",
              icon: "error",
              label: e.$vui.i18n().vuiOrchestra.orchestra.error,
              onClick: t[2] || (t[2] = (g) => o.getStatus("ERROR")),
              class: "text-red"
            }, null, 8, ["label"]),
            K(i, {
              name: "misfired",
              icon: "timer_off",
              label: e.$vui.i18n().vuiOrchestra.orchestra.misfired,
              onClick: t[3] || (t[3] = (g) => o.getStatus("MISFIRED")),
              class: "text-grey"
            }, null, 8, ["label"])
          ]),
          _: 1
        }, 8, ["modelValue"])
      ]),
      Y("div", ei, [
        Y("div", ti, [
          K(u, {
            color: "primary",
            icon: "navigate_before",
            label: e.$vui.i18n().vuiOrchestra.orchestra.previousWeek,
            onClick: t[5] || (t[5] = (g) => o.getWeek(-1))
          }, null, 8, ["label"]),
          K(u, {
            round: "",
            color: "primary",
            icon: "today",
            onClick: t[6] || (t[6] = (g) => o.getWeek(0))
          }),
          K(u, {
            color: "primary",
            "icon-right": "navigate_next",
            label: e.$vui.i18n().vuiOrchestra.orchestra.nextWeek,
            onClick: t[7] || (t[7] = (g) => o.getWeek(1))
          }, null, 8, ["label"])
        ])
      ]),
      Y("div", ni, [
        K(w, {
          rows: s.data,
          columns: s.columns,
          "row-key": "name",
          loading: s.loading
        }, {
          body: me((g) => [
            K(l, {
              props: g,
              onClick: (b) => e.$router.push("/process/" + g.row.processName)
            }, {
              default: me(() => [
                K(d, {
                  key: "processLabel",
                  props: g
                }, {
                  default: me(() => [
                    We(X(g.row.processLabel), 1)
                  ]),
                  _: 2
                }, 1032, ["props"]),
                K(d, {
                  key: "state",
                  props: g
                }, {
                  default: me(() => [
                    Y("div", ri, [
                      Y("div", si, [
                        K(a, {
                          name: "done",
                          size: "sm",
                          class: "text-green"
                        })
                      ]),
                      Y("div", oi, [
                        K(a, {
                          name: "error",
                          size: "sm",
                          class: "text-red"
                        })
                      ]),
                      Y("div", ii, [
                        K(a, {
                          name: "timer_off",
                          size: "sm",
                          class: "text-grey"
                        })
                      ])
                    ]),
                    Y("div", ai, [
                      Y("div", li, X(g.row.successfulCount), 1),
                      Y("div", ci, X(g.row.errorsCount), 1),
                      Y("div", ui, X(g.row.misfiredCount), 1)
                    ])
                  ]),
                  _: 2
                }, 1032, ["props"]),
                K(d, {
                  key: "lastExecutionTime",
                  props: g
                }, {
                  default: me(() => [
                    We(X(g.row.lastExecutionTime), 1)
                  ]),
                  _: 2
                }, 1032, ["props"]),
                K(d, {
                  key: "nextExecutionTime",
                  props: g
                }, {
                  default: me(() => [
                    We(X(g.row.nextExecutionTime), 1)
                  ]),
                  _: 2
                }, 1032, ["props"])
              ]),
              _: 2
            }, 1032, ["props", "onClick"])
          ]),
          "no-data": me(() => [
            Y("div", di, [
              s.loading ? (ze(), He("span", fi, [
                We(X(e.$vui.i18n().vuiOrchestra.orchestra.loading) + " ", 1),
                K(m)
              ])) : s.fail ? (ze(), He("span", hi, X(e.$vui.i18n().vuiOrchestra.orchestra.connectionFailed), 1)) : (ze(), He("span", pi, X(e.$vui.i18n().vuiOrchestra.orchestra.noData), 1))
            ])
          ]),
          _: 1
        }, 8, ["rows", "columns", "loading"])
      ])
    ])
  ]);
}
const nr = /* @__PURE__ */ Ie(Yo, [["render", mi]]), gi = {
  created() {
    L.get(`${this.apiUrl}/definitions/${this.$props.processName}`).then((e) => {
      this.processInfo = e.data, this.form.technical = {
        cronExpression: this.processInfo.triggeringStrategy ? this.processInfo.triggeringStrategy.cronExpression : "",
        active: Object.entries(this.processInfo).length ? this.processInfo.active ? "true" : "false" : "-",
        multiExecution: this.processInfo.triggeringStrategy ? this.processInfo.triggeringStrategy.multiExecution ? "true" : "false" : "-",
        rescuePeriod: this.processInfo.triggeringStrategy ? this.processInfo.triggeringStrategy.rescuePeriodInSeconds : ""
      }, this.form.settings = this.processInfo.triggeringStrategy ? this.processInfo.triggeringStrategy.initialParams ? { ...this.processInfo.triggeringStrategy.initialParams } : {} : {};
    }).catch((e) => {
      console.error(e), e.response ? this.errorMessage = `${this.$vui.i18n().vuiOrchestra.orchestra.noProcess}: ${this.$props.processName}` : this.errorMessage = this.$vui.i18n().vuiOrchestra.orchestra.connectionFailed, this.connectionFailure = !e.response;
    }), L.get(`${this.apiUrl}/executions/summaries/${this.$props.processName}`).then((e) => {
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
      this.errorMessage && (this.connectionFailure == !1 ? this.errorMessage = `${this.$vui.i18n().vuiOrchestra.orchestra.noProcess}: ${this.$props.processName}` : this.connectionFailure == !0 && (this.errorMessage = this.$vui.i18n().vuiOrchestra.orchestra.connectionFailed));
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
        let t = window.Quasar.date.extractDate(
          e.substring(0, 16),
          "YYYY-MM-DDTHH:mm"
        );
        return window.Quasar.date.formatDate(t, "DD/MM/YYYY HH:mm");
      }
      return "";
    },
    executeNow: function(e) {
      L.post(`${this.apiUrl}/executionsControl/executeNow`, { processName: e }).then((t) => {
        this.$q.notify({
          message: this.$vui.i18n().vuiOrchestra.orchestra.executeNowOk,
          color: "positive"
        });
      }).catch((t) => {
        console.error(t), this.$q.notify({
          message: this.$vui.i18n().vuiOrchestra.orchestra.executeNowErr,
          color: "negative"
        });
      });
    },
    fetchActivities: function(e) {
      L.get(`${this.apiUrl}/executions/${e}/activities`).then((t) => {
        this.activities[e] = t.data;
      }).catch((t) => {
        console.error(t);
      });
    },
    updateExecutions: function(e) {
      this.limit = 20, this.offset = 0, this.status = e, this.errorMessage = "", this.connectionFailure = "?", L.get(
        `${this.apiUrl}/executions/?processName=${this.$props.processName}&status=${this.status}&limit=${this.limit}`
      ).then((t) => {
        this.executions = t.data, t.data.map((n) => {
          this.tabs[n.preId] = "info";
        });
      }).catch((t) => {
        console.error(t), t.response ? this.errorMessage = `${this.$vui.i18n().vuiOrchestra.orchestra.noProcess}: ${this.$props.processName}` : this.errorMessage = this.$vui.i18n().vuiOrchestra.orchestra.connectionFailed, this.connectionFailure = !t.response;
      });
    },
    onLoad(e, t) {
      L.get(
        `${this.apiUrl}/executions/?processName=${this.$props.processName}&status=${this.status}&limit=${this.limit}&offset=${this.offset}`
      ).then((n) => {
        this.executions.push(...n.data), n.data.map((r) => {
          this.tabs[r.preId] = "info";
        }), n.data.length < this.limit && (this.executionsLoaded = !0, t(!0)), t(!1);
      }), this.offset += 20;
    },
    onSubmit(e) {
      e == "technical" ? L.put(
        `${this.apiUrl}/definitions/${this.$props.processName}/properties`,
        {
          cronExpression: this.form.technical.cronExpression,
          multiExecution: this.form.technical.multiExecution == "true",
          rescuePeriod: this.form.technical.rescuePeriod,
          active: this.form.technical.active == "true"
        }
      ).then(this.$router.go()).catch(console.error) : e == "settings" && L.put(`${this.apiUrl}/definitions/${this.$props.processName}/params`, {
        initialParams: this.form.settings
      }).then(this.$router.go()).catch(console.error);
    }
  }
}, x = window.Vue.toDisplayString, F = window.Vue.openBlock, H = window.Vue.createElementBlock, ie = window.Vue.createCommentVNode, $ = window.Vue.resolveComponent, f = window.Vue.createVNode, V = window.Vue.createElementVNode, p = window.Vue.withCtx, q = window.Vue.createTextVNode, ue = window.Vue.createBlock, ke = window.Vue.renderList, Ce = window.Vue.Fragment, wi = { class: "q-pa-md text-primary" }, yi = { class: "text-h4 q-mb-md" }, vi = { key: 0 }, bi = { key: 1 }, _i = { class: "text-h6" }, Ei = { class: "col" }, Ri = { class: "text-weight-medium" }, xi = { class: "col" }, Oi = { class: "text-weight-medium" }, Si = { class: "col" }, Ai = { class: "text-weight-medium" }, Ti = { class: "text-h6" }, ki = { class: "text-h6" }, Ci = { key: 0 }, qi = { class: "q-gutter-sm" }, Pi = { class: "q-gutter-sm" }, Ni = { key: 1 }, Vi = { class: "text-h6" }, Ui = { key: 0 }, Li = { key: 0 }, Fi = { key: 1 }, Bi = { class: "q-pa-md" }, Di = { class: "text-h4 q-gutter-x-md q-mx-auto row" }, Ii = { class: "col" }, Mi = { class: "col-auto" }, $i = { class: "text-h5" }, ji = {
  style: { "max-width": "400px" },
  class: "text-primary"
}, Wi = { class: "text-weight-medium" }, zi = { class: "text-weight-medium" }, Hi = { class: "text-weight-medium" }, Ki = { class: "text-h5" }, Yi = { style: { "max-width": "400px" } }, Ji = { class: "text-weight-medium" }, Gi = { class: "text-weight-medium" }, Qi = { class: "text-weight-medium" }, Zi = { class: "text-weight-medium" }, Xi = { class: "text-weight-medium" }, ea = { class: "row justify-center q-my-md" }, ta = { key: 0 }, na = {
  key: 1,
  class: "text-weight-medium"
};
function ra(e, t, n, r, s, o) {
  const i = $("q-spinner-ios"), c = $("q-card-section"), u = $("q-separator"), d = $("q-icon"), a = $("q-item-section"), l = $("q-item"), m = $("q-list"), w = $("q-btn"), g = $("q-card"), b = $("q-input"), v = $("q-radio"), O = $("q-form"), R = $("q-tab"), A = $("q-tabs"), I = $("q-avatar"), B = $("q-tooltip"), j = $("q-tab-panel"), M = $("q-expansion-item"), fe = $("q-tab-panels"), le = $("q-splitter"), Me = $("q-infinite-scroll");
  return F(), ue(le, {
    modelValue: s.splitterModel,
    "onUpdate:modelValue": t[16] || (t[16] = (T) => s.splitterModel = T),
    style: { height: "85vh" },
    limits: [0, 100]
  }, {
    before: p(() => [
      V("div", wi, [
        V("div", yi, [
          s.processInfo.label || s.errorMessage ? (F(), H("div", vi, x(s.processInfo.label || s.errorMessage), 1)) : (F(), H("div", bi, [
            f(i, {
              color: "primary",
              size: "sm"
            })
          ]))
        ]),
        f(g, { class: "my-card" }, {
          default: p(() => [
            f(c, null, {
              default: p(() => [
                V("div", _i, x(e.$vui.i18n().vuiOrchestra.orchestra.totalExecutions), 1)
              ]),
              _: 1
            }),
            f(u, { inset: "" }),
            f(c, { class: "row" }, {
              default: p(() => [
                V("div", Ei, [
                  f(m, null, {
                    default: p(() => [
                      f(l, null, {
                        default: p(() => [
                          f(a, null, {
                            default: p(() => [
                              V("div", Ri, [
                                f(d, {
                                  color: "green",
                                  name: "done",
                                  size: "sm"
                                }),
                                q(" " + x(s.processSummary.successfulCount), 1)
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
                  f(m, null, {
                    default: p(() => [
                      f(l, null, {
                        default: p(() => [
                          f(a, null, {
                            default: p(() => [
                              V("div", Oi, [
                                f(d, {
                                  color: "red",
                                  name: "error",
                                  size: "sm"
                                }),
                                q(" " + x(s.processSummary.errorsCount), 1)
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
                V("div", Si, [
                  f(m, null, {
                    default: p(() => [
                      f(l, null, {
                        default: p(() => [
                          f(a, null, {
                            default: p(() => [
                              V("div", Ai, [
                                f(d, {
                                  color: "grey",
                                  name: "timer_off",
                                  size: "sm"
                                }),
                                q(" " + x(s.processSummary.misfiredCount), 1)
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
            f(u, { inset: "" }),
            n.orchestraUiReadOnly ? ie("", !0) : (F(), ue(c, { key: 0 }, {
              default: p(() => [
                f(w, {
                  label: e.$vui.i18n().vuiOrchestra.orchestra.executeNow,
                  onClick: t[0] || (t[0] = (T) => o.executeNow(s.processInfo.definitionId)),
                  color: "indigo",
                  class: "q-mx-auto"
                }, null, 8, ["label"])
              ]),
              _: 1
            }))
          ]),
          _: 1
        }),
        s.processInfo.metadatas && Object.keys(s.processInfo.metadatas).length > 0 ? (F(), ue(g, {
          key: 0,
          class: "my-card q-mt-lg text-primary"
        }, {
          default: p(() => [
            f(c, null, {
              default: p(() => [
                V("div", Ti, x(e.$vui.i18n().vuiOrchestra.orchestra.functionalId), 1)
              ]),
              _: 1
            }),
            f(u, { inset: "" }),
            f(c, null, {
              default: p(() => [
                f(m, { dense: "" }, {
                  default: p(() => [
                    (F(!0), H(Ce, null, ke(s.processInfo.metadatas, (T, P) => (F(), H("div", { key: P }, [
                      f(l, null, {
                        default: p(() => [
                          f(a, { class: "text-weight-medium" }, {
                            default: p(() => [
                              q(x(P), 1)
                            ]),
                            _: 2
                          }, 1024),
                          f(a, { side: "" }, {
                            default: p(() => [
                              q(x(T), 1)
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
        })) : ie("", !0),
        f(g, { class: "my-card q-mt-lg text-primary" }, {
          default: p(() => [
            f(c, null, {
              default: p(() => [
                V("div", ki, [
                  q(x(e.$vui.i18n().vuiOrchestra.orchestra.technicalId) + " ", 1),
                  n.orchestraUiReadOnly ? ie("", !0) : (F(), ue(w, {
                    key: 0,
                    round: "",
                    color: "indigo",
                    icon: "edit",
                    class: "q-ml-sm",
                    size: "sm",
                    onClick: t[1] || (t[1] = (T) => s.editMode.technical = !s.editMode.technical)
                  }))
                ])
              ]),
              _: 1
            }),
            f(u, { inset: "" }),
            f(c, { class: "q-gutter-sm" }, {
              default: p(() => [
                V("div", null, [
                  f(m, { dense: "" }, {
                    default: p(() => [
                      s.editMode.technical ? (F(), H("div", Ci, [
                        f(O, {
                          onSubmit: t[8] || (t[8] = (T) => o.onSubmit("technical"))
                        }, {
                          default: p(() => [
                            f(l, null, {
                              default: p(() => [
                                f(a, { class: "text-weight-medium" }, {
                                  default: p(() => [
                                    q(x(e.$vui.i18n().vuiOrchestra.orchestra.processName), 1)
                                  ]),
                                  _: 1
                                }),
                                f(a, { side: "" }, {
                                  default: p(() => [
                                    q(x(s.processInfo.definitionId), 1)
                                  ]),
                                  _: 1
                                })
                              ]),
                              _: 1
                            }),
                            f(l, null, {
                              default: p(() => [
                                f(a, { class: "text-weight-medium" }, {
                                  default: p(() => [
                                    q(x(e.$vui.i18n().vuiOrchestra.orchestra.cronExpression), 1)
                                  ]),
                                  _: 1
                                }),
                                f(a, { side: "" }, {
                                  default: p(() => [
                                    f(b, {
                                      modelValue: s.form.technical.cronExpression,
                                      "onUpdate:modelValue": t[2] || (t[2] = (T) => s.form.technical.cronExpression = T),
                                      dense: !0,
                                      placeholder: "* * * * * ? *"
                                    }, null, 8, ["modelValue"])
                                  ]),
                                  _: 1
                                })
                              ]),
                              _: 1
                            }),
                            f(l, null, {
                              default: p(() => [
                                f(a, { class: "text-weight-medium" }, {
                                  default: p(() => [
                                    q(x(e.$vui.i18n().vuiOrchestra.orchestra.active), 1)
                                  ]),
                                  _: 1
                                }),
                                f(a, { side: "" }, {
                                  default: p(() => [
                                    V("div", qi, [
                                      f(v, {
                                        size: "xs",
                                        modelValue: s.form.technical.active,
                                        "onUpdate:modelValue": t[3] || (t[3] = (T) => s.form.technical.active = T),
                                        val: "true",
                                        label: e.$vui.i18n().vuiOrchestra.orchestra.yes
                                      }, null, 8, ["modelValue", "label"]),
                                      f(v, {
                                        size: "xs",
                                        modelValue: s.form.technical.active,
                                        "onUpdate:modelValue": t[4] || (t[4] = (T) => s.form.technical.active = T),
                                        val: "false",
                                        label: e.$vui.i18n().vuiOrchestra.orchestra.no
                                      }, null, 8, ["modelValue", "label"])
                                    ])
                                  ]),
                                  _: 1
                                })
                              ]),
                              _: 1
                            }),
                            f(l, null, {
                              default: p(() => [
                                f(a, { class: "text-weight-medium" }, {
                                  default: p(() => [
                                    q(x(e.$vui.i18n().vuiOrchestra.orchestra.multiExecution), 1)
                                  ]),
                                  _: 1
                                }),
                                f(a, { side: "" }, {
                                  default: p(() => [
                                    V("div", Pi, [
                                      f(v, {
                                        size: "xs",
                                        modelValue: s.form.technical.multiExecution,
                                        "onUpdate:modelValue": t[5] || (t[5] = (T) => s.form.technical.multiExecution = T),
                                        val: "true",
                                        label: e.$vui.i18n().vuiOrchestra.orchestra.yes
                                      }, null, 8, ["modelValue", "label"]),
                                      f(v, {
                                        size: "xs",
                                        modelValue: s.form.technical.multiExecution,
                                        "onUpdate:modelValue": t[6] || (t[6] = (T) => s.form.technical.multiExecution = T),
                                        val: "false",
                                        label: e.$vui.i18n().vuiOrchestra.orchestra.no
                                      }, null, 8, ["modelValue", "label"])
                                    ])
                                  ]),
                                  _: 1
                                })
                              ]),
                              _: 1
                            }),
                            f(l, null, {
                              default: p(() => [
                                f(a, { class: "text-weight-medium" }, {
                                  default: p(() => [
                                    q(x(e.$vui.i18n().vuiOrchestra.orchestra.rescuePeriod), 1)
                                  ]),
                                  _: 1
                                }),
                                f(a, { side: "" }, {
                                  default: p(() => [
                                    f(b, {
                                      modelValue: s.form.technical.rescuePeriod,
                                      "onUpdate:modelValue": t[7] || (t[7] = (T) => s.form.technical.rescuePeriod = T),
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
                              f(l, null, {
                                default: p(() => [
                                  n.orchestraUiReadOnly ? ie("", !0) : (F(), ue(w, {
                                    key: 0,
                                    label: e.$vui.i18n().vuiOrchestra.orchestra.submit,
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
                      ])) : (F(), H("div", Ni, [
                        f(l, null, {
                          default: p(() => [
                            f(a, { class: "text-weight-medium" }, {
                              default: p(() => [
                                q(x(e.$vui.i18n().vuiOrchestra.orchestra.processName), 1)
                              ]),
                              _: 1
                            }),
                            f(a, { side: "" }, {
                              default: p(() => [
                                q(x(s.processInfo.definitionId), 1)
                              ]),
                              _: 1
                            })
                          ]),
                          _: 1
                        }),
                        f(l, null, {
                          default: p(() => [
                            f(a, { class: "text-weight-medium" }, {
                              default: p(() => [
                                q(x(e.$vui.i18n().vuiOrchestra.orchestra.cronExpression), 1)
                              ]),
                              _: 1
                            }),
                            f(a, { side: "" }, {
                              default: p(() => [
                                q(x(s.processInfo.triggeringStrategy ? s.processInfo.triggeringStrategy.cronExpression : "-"), 1)
                              ]),
                              _: 1
                            })
                          ]),
                          _: 1
                        }),
                        f(l, null, {
                          default: p(() => [
                            f(a, { class: "text-weight-medium" }, {
                              default: p(() => [
                                q(x(e.$vui.i18n().vuiOrchestra.orchestra.active), 1)
                              ]),
                              _: 1
                            }),
                            f(a, { side: "" }, {
                              default: p(() => [
                                q(x(Object.entries(s.processInfo).length ? s.processInfo.active ? e.$vui.i18n().vuiOrchestra.orchestra.yes : e.$vui.i18n().vuiOrchestra.orchestra.no : "-"), 1)
                              ]),
                              _: 1
                            })
                          ]),
                          _: 1
                        }),
                        f(l, null, {
                          default: p(() => [
                            f(a, { class: "text-weight-medium" }, {
                              default: p(() => [
                                q(x(e.$vui.i18n().vuiOrchestra.orchestra.multiExecution), 1)
                              ]),
                              _: 1
                            }),
                            f(a, { side: "" }, {
                              default: p(() => [
                                q(x(s.processInfo.triggeringStrategy ? s.processInfo.triggeringStrategy.multiExecution ? e.$vui.i18n().vuiOrchestra.orchestra.yes : e.$vui.i18n().vuiOrchestra.orchestra.no : "-"), 1)
                              ]),
                              _: 1
                            })
                          ]),
                          _: 1
                        }),
                        f(l, null, {
                          default: p(() => [
                            f(a, { class: "text-weight-medium" }, {
                              default: p(() => [
                                q(x(e.$vui.i18n().vuiOrchestra.orchestra.rescuePeriod), 1)
                              ]),
                              _: 1
                            }),
                            f(a, { side: "" }, {
                              default: p(() => [
                                q(x(s.processInfo.triggeringStrategy ? s.processInfo.triggeringStrategy.rescuePeriodInSeconds : "-"), 1)
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
        f(g, { class: "my-card q-mt-lg text-primary" }, {
          default: p(() => [
            f(c, null, {
              default: p(() => [
                V("div", Vi, [
                  q(x(e.$vui.i18n().vuiOrchestra.orchestra.settings) + " ", 1),
                  n.orchestraUiReadOnly ? ie("", !0) : (F(), ue(w, {
                    key: 0,
                    round: "",
                    color: "indigo",
                    icon: "edit",
                    class: "q-ml-sm",
                    size: "sm",
                    onClick: t[9] || (t[9] = (T) => s.editMode.settings = !s.editMode.settings)
                  }))
                ])
              ]),
              _: 1
            }),
            f(u, { inset: "" }),
            f(c, null, {
              default: p(() => [
                f(m, { dense: "" }, {
                  default: p(() => [
                    s.processInfo.triggeringStrategy && s.processInfo.triggeringStrategy.initialParams ? (F(), H("div", Ui, [
                      s.editMode.settings ? (F(), H("div", Li, [
                        f(O, {
                          onSubmit: t[10] || (t[10] = (T) => o.onSubmit("settings"))
                        }, {
                          default: p(() => [
                            (F(!0), H(Ce, null, ke(s.processInfo.triggeringStrategy.initialParams, (T, P) => (F(), H("div", { key: P }, [
                              f(l, null, {
                                default: p(() => [
                                  f(a, { class: "text-weight-medium" }, {
                                    default: p(() => [
                                      q(x(P), 1)
                                    ]),
                                    _: 2
                                  }, 1024),
                                  f(a, { side: "" }, {
                                    default: p(() => [
                                      f(b, {
                                        modelValue: s.form.settings[P],
                                        "onUpdate:modelValue": (se) => s.form.settings[P] = se,
                                        dense: !0,
                                        rules: [
                                          (se) => se && se.length > 0 || e.$vui.i18n().vuiOrchestra.orchestra.fieldCannotBeEmpty
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
                              f(l, null, {
                                default: p(() => [
                                  n.orchestraUiReadOnly ? ie("", !0) : (F(), ue(w, {
                                    key: 0,
                                    label: e.$vui.i18n().vuiOrchestra.orchestra.submit,
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
                      ])) : (F(), H("div", Fi, [
                        (F(!0), H(Ce, null, ke(s.processInfo.triggeringStrategy.initialParams, (T, P) => (F(), H("div", { key: P }, [
                          f(l, null, {
                            default: p(() => [
                              f(a, { class: "text-weight-medium" }, {
                                default: p(() => [
                                  q(x(P), 1)
                                ]),
                                _: 2
                              }, 1024),
                              f(a, { side: "" }, {
                                default: p(() => [
                                  q(x(T), 1)
                                ]),
                                _: 2
                              }, 1024)
                            ]),
                            _: 2
                          }, 1024)
                        ]))), 128))
                      ]))
                    ])) : ie("", !0)
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
      V("div", Bi, [
        V("div", Di, [
          V("div", Ii, x(e.$vui.i18n().vuiOrchestra.orchestra.executions), 1),
          V("div", Mi, [
            f(A, {
              modelValue: s.filterTab,
              "onUpdate:modelValue": t[14] || (t[14] = (T) => s.filterTab = T),
              "inline-label": "",
              class: "text-primary q-mx-auto"
            }, {
              default: p(() => [
                f(R, {
                  name: "all",
                  icon: "list",
                  label: e.$vui.i18n().vuiOrchestra.orchestra.all,
                  onClick: t[11] || (t[11] = (T) => o.updateExecutions(""))
                }, null, 8, ["label"]),
                f(R, {
                  name: "done",
                  icon: "done",
                  label: e.$vui.i18n().vuiOrchestra.orchestra.done,
                  onClick: t[12] || (t[12] = (T) => o.updateExecutions("DONE")),
                  class: "text-green"
                }, null, 8, ["label"]),
                f(R, {
                  name: "error",
                  icon: "error",
                  label: e.$vui.i18n().vuiOrchestra.orchestra.error,
                  onClick: t[13] || (t[13] = (T) => o.updateExecutions("ERROR")),
                  class: "text-red"
                }, null, 8, ["label"])
              ]),
              _: 1
            }, 8, ["modelValue"])
          ])
        ]),
        f(Me, {
          onLoad: o.onLoad,
          offset: 50,
          "scroll-target": "div.q-splitter__panel.q-splitter__after.col"
        }, {
          loading: p(() => [
            V("div", ea, [
              !s.errorMessage && !s.executionsLoaded ? (F(), H("div", ta, [
                f(i, {
                  color: "primary",
                  size: "2em"
                })
              ])) : (F(), H("div", na, x(s.errorMessage), 1))
            ])
          ]),
          default: p(() => [
            f(m, {
              bordered: "",
              class: "rounded-borders q-mt-sm"
            }, {
              default: p(() => [
                (F(!0), H(Ce, null, ke(s.executions, (T) => (F(), H("div", {
                  key: T.preId
                }, [
                  f(M, {
                    "expand-separator": "",
                    onShow: (P) => o.fetchActivities(T.preId)
                  }, {
                    header: p(() => [
                      f(a, { avatar: "" }, {
                        default: p(() => [
                          f(I, {
                            icon: o.getIconFromExecutionState(T.status),
                            color: o.getColorFromExecutionState(T.status),
                            "text-color": "white"
                          }, null, 8, ["icon", "color"]),
                          f(B, null, {
                            default: p(() => [
                              q(x(T.status), 1)
                            ]),
                            _: 2
                          }, 1024)
                        ]),
                        _: 2
                      }, 1024),
                      f(a, { class: "text-weight-medium" }, {
                        default: p(() => [
                          q(x(o.formatDate(T.beginTime)) + " ", 1),
                          f(B, {
                            delay: 1e3,
                            anchor: "top middle"
                          }, {
                            default: p(() => [
                              q(" preId: " + x(T.preId), 1)
                            ]),
                            _: 2
                          }, 1024)
                        ]),
                        _: 2
                      }, 1024)
                    ]),
                    default: p(() => [
                      f(g, null, {
                        default: p(() => [
                          f(u),
                          f(c, null, {
                            default: p(() => [
                              f(le, {
                                modelValue: s.splitterModelExecutions,
                                "onUpdate:modelValue": t[15] || (t[15] = (P) => s.splitterModelExecutions = P)
                              }, {
                                before: p(() => [
                                  f(A, {
                                    modelValue: s.tabs[T.preId],
                                    "onUpdate:modelValue": (P) => s.tabs[T.preId] = P,
                                    vertical: "",
                                    class: "text-primary"
                                  }, {
                                    default: p(() => [
                                      f(R, {
                                        name: "info",
                                        label: e.$vui.i18n().vuiOrchestra.orchestra.informations
                                      }, null, 8, ["label"]),
                                      f(R, {
                                        name: "activities",
                                        label: e.$vui.i18n().vuiOrchestra.orchestra.activities
                                      }, null, 8, ["label"]),
                                      f(R, {
                                        name: "support",
                                        label: e.$vui.i18n().vuiOrchestra.orchestra.support
                                      }, null, 8, ["label"])
                                    ]),
                                    _: 2
                                  }, 1032, ["modelValue", "onUpdate:modelValue"])
                                ]),
                                after: p(() => [
                                  f(fe, {
                                    modelValue: s.tabs[T.preId],
                                    "onUpdate:modelValue": (P) => s.tabs[T.preId] = P,
                                    animated: "",
                                    vertical: "",
                                    "transition-prev": "jump-up",
                                    "transition-next": "jump-up"
                                  }, {
                                    default: p(() => [
                                      f(j, { name: "info" }, {
                                        default: p(() => [
                                          V("div", $i, x(e.$vui.i18n().vuiOrchestra.orchestra.informations), 1),
                                          f(u, { class: "q-mt-sm q-mb-md" }),
                                          V("div", ji, [
                                            f(m, { dense: "" }, {
                                              default: p(() => [
                                                f(l, null, {
                                                  default: p(() => [
                                                    f(a, null, {
                                                      default: p(() => [
                                                        V("div", Wi, x(e.$vui.i18n().vuiOrchestra.orchestra.startTime), 1)
                                                      ]),
                                                      _: 1
                                                    }),
                                                    f(a, {
                                                      side: "",
                                                      class: "text-primary"
                                                    }, {
                                                      default: p(() => [
                                                        q(x(o.formatDate(T.beginTime)), 1)
                                                      ]),
                                                      _: 2
                                                    }, 1024)
                                                  ]),
                                                  _: 2
                                                }, 1024),
                                                f(l, null, {
                                                  default: p(() => [
                                                    f(a, null, {
                                                      default: p(() => [
                                                        V("div", zi, x(e.$vui.i18n().vuiOrchestra.orchestra.endTime), 1)
                                                      ]),
                                                      _: 1
                                                    }),
                                                    f(a, {
                                                      side: "",
                                                      class: "text-primary"
                                                    }, {
                                                      default: p(() => [
                                                        q(x(o.formatDate(T.endTime)), 1)
                                                      ]),
                                                      _: 2
                                                    }, 1024)
                                                  ]),
                                                  _: 2
                                                }, 1024),
                                                f(l, null, {
                                                  default: p(() => [
                                                    f(a, null, {
                                                      default: p(() => [
                                                        V("div", Hi, x(e.$vui.i18n().vuiOrchestra.orchestra.duration), 1)
                                                      ]),
                                                      _: 1
                                                    }),
                                                    f(a, {
                                                      side: "",
                                                      class: "text-primary"
                                                    }, {
                                                      default: p(() => [
                                                        q(x(T.executionTime) + "s ", 1)
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
                                      f(j, { name: "activities" }, {
                                        default: p(() => [
                                          V("div", Ki, x(e.$vui.i18n().vuiOrchestra.orchestra.activities), 1),
                                          f(u, { class: "q-mt-sm q-mb-md" }),
                                          f(m, {
                                            bordered: "",
                                            class: "rounded-borders"
                                          }, {
                                            default: p(() => [
                                              (F(!0), H(Ce, null, ke(s.activities[T.preId], (P) => (F(), H("div", {
                                                key: P.aceId
                                              }, [
                                                f(M, { "expand-separator": "" }, {
                                                  header: p(() => [
                                                    f(a, { avatar: "" }, {
                                                      default: p(() => [
                                                        f(I, {
                                                          icon: o.getIconFromExecutionState(P.status),
                                                          color: o.getColorFromExecutionState(P.status),
                                                          "text-color": "white"
                                                        }, null, 8, ["icon", "color"]),
                                                        f(B, null, {
                                                          default: p(() => [
                                                            q(x(P.status), 1)
                                                          ]),
                                                          _: 2
                                                        }, 1024)
                                                      ]),
                                                      _: 2
                                                    }, 1024),
                                                    f(a, { class: "text-weight-medium" }, {
                                                      default: p(() => [
                                                        q(x(P.label) + " ", 1),
                                                        f(B, {
                                                          delay: 1e3,
                                                          anchor: "top middle"
                                                        }, {
                                                          default: p(() => [
                                                            q(" aceId: " + x(P.aceId), 1)
                                                          ]),
                                                          _: 2
                                                        }, 1024)
                                                      ]),
                                                      _: 2
                                                    }, 1024)
                                                  ]),
                                                  default: p(() => [
                                                    f(g, null, {
                                                      default: p(() => [
                                                        f(u),
                                                        f(c, null, {
                                                          default: p(() => [
                                                            V("div", Yi, [
                                                              f(m, { dense: "" }, {
                                                                default: p(() => [
                                                                  f(l, null, {
                                                                    default: p(() => [
                                                                      f(a, null, {
                                                                        default: p(() => [
                                                                          V("div", Ji, x(e.$vui.i18n().vuiOrchestra.orchestra.startTime), 1)
                                                                        ]),
                                                                        _: 1
                                                                      }),
                                                                      f(a, {
                                                                        side: "",
                                                                        class: "text-primary"
                                                                      }, {
                                                                        default: p(() => [
                                                                          q(x(o.formatDate(P.beginTime)), 1)
                                                                        ]),
                                                                        _: 2
                                                                      }, 1024)
                                                                    ]),
                                                                    _: 2
                                                                  }, 1024),
                                                                  f(l, null, {
                                                                    default: p(() => [
                                                                      f(a, null, {
                                                                        default: p(() => [
                                                                          V("div", Gi, x(e.$vui.i18n().vuiOrchestra.orchestra.endTime), 1)
                                                                        ]),
                                                                        _: 1
                                                                      }),
                                                                      f(a, {
                                                                        side: "",
                                                                        class: "text-primary"
                                                                      }, {
                                                                        default: p(() => [
                                                                          q(x(o.formatDate(P.endTime)), 1)
                                                                        ]),
                                                                        _: 2
                                                                      }, 1024)
                                                                    ]),
                                                                    _: 2
                                                                  }, 1024),
                                                                  f(l, null, {
                                                                    default: p(() => [
                                                                      f(a, null, {
                                                                        default: p(() => [
                                                                          V("div", Qi, x(e.$vui.i18n().vuiOrchestra.orchestra.duration), 1)
                                                                        ]),
                                                                        _: 1
                                                                      }),
                                                                      f(a, {
                                                                        side: "",
                                                                        class: "text-primary"
                                                                      }, {
                                                                        default: p(() => [
                                                                          q(x(P.executionTime) + "s ", 1)
                                                                        ]),
                                                                        _: 2
                                                                      }, 1024)
                                                                    ]),
                                                                    _: 2
                                                                  }, 1024),
                                                                  P.hasLogFile ? (F(), ue(l, { key: 0 }, {
                                                                    default: p(() => [
                                                                      f(a, null, {
                                                                        default: p(() => [
                                                                          V("div", Zi, x(e.$vui.i18n().vuiOrchestra.orchestra.logFile), 1)
                                                                        ]),
                                                                        _: 1
                                                                      }),
                                                                      f(a, {
                                                                        side: "",
                                                                        class: "text-primary"
                                                                      }, {
                                                                        default: p(() => [
                                                                          f(w, {
                                                                            type: "a",
                                                                            icon: "description",
                                                                            flat: "",
                                                                            padding: "none",
                                                                            title: e.$vui.i18n().vuiOrchestra.orchestra.logFile,
                                                                            href: n.apiUrl + "/executions/" + T.preId + "/activities/" + P.aceId + "/attachment"
                                                                          }, null, 8, ["title", "href"])
                                                                        ]),
                                                                        _: 2
                                                                      }, 1024)
                                                                    ]),
                                                                    _: 2
                                                                  }, 1024)) : ie("", !0),
                                                                  P.hasTechnicalLog ? (F(), ue(l, { key: 1 }, {
                                                                    default: p(() => [
                                                                      f(a, null, {
                                                                        default: p(() => [
                                                                          V("div", Xi, x(e.$vui.i18n().vuiOrchestra.orchestra.technicalLogFile), 1)
                                                                        ]),
                                                                        _: 1
                                                                      }),
                                                                      f(a, {
                                                                        side: "",
                                                                        class: "text-primary"
                                                                      }, {
                                                                        default: p(() => [
                                                                          f(w, {
                                                                            type: "a",
                                                                            icon: "description",
                                                                            flat: "",
                                                                            padding: "none",
                                                                            title: e.$vui.i18n().vuiOrchestra.orchestra.technicalLogFile,
                                                                            href: n.apiUrl + "/executions/" + T.preId + "/activities/" + P.aceId + "/logFile"
                                                                          }, null, 8, ["title", "href"])
                                                                        ]),
                                                                        _: 2
                                                                      }, 1024)
                                                                    ]),
                                                                    _: 2
                                                                  }, 1024)) : ie("", !0)
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
                                                f(u)
                                              ]))), 128))
                                            ]),
                                            _: 2
                                          }, 1024)
                                        ]),
                                        _: 2
                                      }, 1024),
                                      ie("", !0)
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
                  f(u)
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
const rr = /* @__PURE__ */ Ie(gi, [["render", ra]]), sr = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEYAAABGCAYAAABxLuKEAAALlklEQVR4nOxc6W9c5dU/d7+zefaxPXa8O4kJcTDgkDcvLwECL4bAGxISB6K3pAWpVdU/gA8JEFBaAqWhUYC2iCKVIlREi1pRqaWiRSzqAqGLQhYq8MRL7MT27DN3mbtVz9iT2LPeO3Nn7A/8pCi+9z5zzrlnzvY8z3mG1DQNvkIh8JUWYLXiK8WUALn4f9tcMvPjuVTGNMJdXst7Vpo4ZhrBIlA1eDidke81hZa6EFKcFuoRADiD5WLM2Dz322+9eWaHWRHnO/+95tKuweY2AFBMIlmAcDpz+sswf5XRzwmSosZ4GWK8lP0X52U8Icjwza1rPu73226AJRYDPT7roZt73Xe+92XUFPd6/4tI867B5m0A8Ccz6BXBhggnlVWKomqQFGQ1JmRfflERMoiyWvCOfT4r9PttB3PX5JJn/zwwHPzlB2PRUcUEszl9KQ1zqcz9fjtdF8UoKuyNCfLla15S1PhyK4CEKOOapi+O3jngew8A3s1dY3npuu/4++dPv31mnjZD+G9vbY/et6mlFQBEM+gtxfkwd+735+b7kQvEBRkXZbVqWhtb7eqBze1bAOCT3L18bX7x4HDbSyxpTrKK87IbAEZMIbYc105EU+tCYQ6fTWVqUgqOAdx1lf/NpUqBYunabaW+u2tjgKuaU46OhQTAMGTWo7XSykdGVkfn0iJ4bWTNTn/9GqfstzOP598vZhoXR69ped5OEzUx7PVas0KHwvw9AGCpiVgeLsS5UZRd/fbaFENgALev870KAJ/nPyvqMw6WfHrvpuZ4LQy7PBYM/R2K8A4U26qlVQTDkzGuG/1hZwjcQmFVK+f6DqfotlJPFntWKphEdg82H3MyZFUMg05GoxfjFEqPUU7aWxWhIhBlZd9s8kosD1RpNSi23Nrv/TkAjBd9XuqDFpp4bvdgIFwN0+5Fa8nhfIS/GwCs1dDKA3Yhxu9ZqgmvjcSxKgih2OK10U+Xel4u/SR3bgwcNxpr0PjWJnbZvbEwbzfJnbZMxbjOpTcoAgenBTeUlpAqF2PLF6XGlM3LdoY8YTTWdHlYIPK+w7iQdad9RugUgyApe2dThSWRz2bM5bd0ukrGlhwqFSyx3YPNP3Cx+hl3eSxFfT4U5u6q0Z2wC3FubzHiLguJ6/UnmsBg+1rvS6ViSw4VKzkLTfzwgWtb5vQw9dkotYmliooYivA2ALhbD50S2DoZ49uLPUAW6rUSutzpxh532sGS36s0Tk+Jm7xnQ+Bpv42qOLDXay35vcUFBSLpTNXFniApo/NF3CgHv72yfBYSh5v7vCdQrVZprK7anybxF/7/utYL5caggNbpZssadCjCowBs08MzD/hUjNtTLi/bGRxnyfL+tK3PE7PSxDO6GOoUTBgZCDzW6WJLDuj2WIAkypMbC/MoxuzSyXMptk1EuWC5ARiGQcBBlnSnJpaA/+n1HAWAqB6GumeLBA6vPjgcPFfqea/XUtHHk6ICc6mM4WKPy8j7wlzl1UVfmZpme79vmiHx5/XyNDKNlrf1eQ6u9xcmFq+VBI+N1kVrLMyh2bbHAF9qMsrt0TWQwMFdJAj7rBRs6XKj9JzWy9To+sJbD93QdjL/Zq9PfxY+H+ZpANhpgOdtkzHOq3ew315YWvzvet+XBA4/NcDT+C7BtWuchzYF7Zev0ZSo061/8pyWVLiUFHVnp5Qo7Y3ykm76TpbAUa2SQ4uDhmvWOA8ji9dNpMrtk3ce2tz2Qe6iw2XJmrARhML8duSBOobSUzHeULBGQdhnuzKNGRnwf4YDvG5IwGr3lTa0Og5u6XBm/+4z4EY5nI/wqOjQs+1x+2SMcxmlH1isaTpcLFzd6ngUAAwv8VW7hvnRNzYH30ZTBa+Owi8fnKTCxYR4X6VxSUG6L2bAjXJgKBwcDK6ODPj+CgC/Nkyglp3IXr/tia8PBzVkutUgFMm6k7vMEHoqxlW9mTbU5sDXBuwHq/18Laven25f532jw8VoRBW6OR/JZqfdZYagbFROcUUhqxqMzQvqYND5bi17WjVtB1go4vEen1Xp91vBweibxOXASyrMJMSSxV5CkPbFBUOJBOK8rJ2a5sFnZ/Eur7VqawETNvX/3eZkfoayUofHgrc7GdXIcloozN+K6q8ij5ipGPd/eukoqgahsKiemxWxjKLByHr/bwDgY/2SFKLmDSSGJJ702ykR6cNlpXCUpWw0oWsddjzKU2rxudMdk1F92SglKtpnM7w2m5Kz77Ip6FDbXOyjRt8jH2bsrE0EnexPcjGYJnHo8rBYi4NWKxkPcqeLcaHAnRJ8ZjQhlncjVdNgMppRT18UMEHWsqyQDHes9/0CAE7V8kJgVn8MTeBHWhz05SXQbJFlp/E+f9Z6ysaesTB/CypQl9yyTkTLu1FSUNTPZniYTkjL5L+hwykGHEzN1gImNg7NtTqY4/kFMLNgPXhLE10y9oxHeVJRl2WnuyZj2b2oAqBYgqzk7CUB56Xl3krhGNy21ofmQ2O1v46JHVUkgX8/2MTM5t9fKNFLW48oazAd5/fnriNpcX8qU+hGSWEhliArKRbAbuxxp1xW6glz3sbcVrNUs509WmpFhCYWrKe1qTBzhSL8fwFAEADsUzF+2TaLqmowERXVM5euxJJ8MCQGt/R7jwNAwRdTLUztwcNx+FGrg54u9RxZj9e2kLms1JW9oPGogCsqoLgyMhnjLi8Tooxz6iKvzSTksnJu7XLHrDTxrImvYnpzohBwsM/SFUphlLm6vRY8l7kkRYOZhLAzyok7OUlZnnGk4laSA0VgcFOv5wUAiJn5IvmNQ2bAdikpnhqPCt16Bouyqk3FBOh0s5kmFsucnIzZx+ZF4CooJIdb+jypHRsCiNd8zZIvQT3aWdN+O/MspbMEZkgc6/FaMEFWmZMTCcfpGQHTqxSWxFFsOWG2UqBefb44Bq8EnUzJWJMPFHsuJkT4cCwORux3W6/H9NiSQ70aoAW/gznCGJh2/208rs2n9a+92GgCbup1H0MZvkoZy6JuneE4wMtBJxvSMzbBS3B2No2lJRV4SV9b8K39njmGIp6rVc5SqGfLvOS304f1NDr+ZTyea8yGuZRUcfnCyZKwtdt9FGV0UyQtgnqfJXit3cWcKTdAUlT4ZCJ+WY4oL+PoXjncttY7RRH4iybKWYB6K0b1WOnHrFRpNv+YSqjcEiPRsq3wpWON10rB5k73ERTHTJd2CRpx+uStdhf7abEHqIb6c6iwLpvnpGyRVwyLm2evmC/mcjRCMZrLQj3iYApb1j6fTauzaalABlkFiHJygWaCTQwMrXEeRB5YR3mzaNR5pT92uNh38m9+OFa68WA2lYH8qnzHVf6TOMAb9RFxORp2kMvGkIey3eKLmI4LaigilOQvKhqWFK9E4R6vBdY1V78dYhSNPOF2st3JvpW7KBZb8jGXzlyuEO8c8L8PAH+oo3zL0NCjfxaaeNRrpZSkIMO/ppMVeSdFFRMkBdYHbNDttR5qjJQLaPSZyDNtTub1TybioPdMFJomjAz4fwcAH9VbuKVo+GFRliIO22hC9+HLDS0Otd3FNtRaYIVO0Y7tv671FT0TTBwDODAc/BUA/L0hki3l3WiGkO3/p4/svDrAVxp3e79X6fBYCs4SNQIrde76wv1DLS+WmyqQOAZfGw6+BgBnGyrZIlbsQHqThTq6Z7A5Wer5jgFfpqWJMW07xChW8qT+/J5rmp9rKjJVQPFn/3WtLwOArvWcemBFf8LASpPH7h9qKTgTde/VAQ7FoZWRagEr/dsO8Z0bA894rVfa1VDc2TfU8gIAzKykYCutGGBI4sSB4eBE7vqBodZIk4V6amWlWgWKAQB+ZL3/cLuTAY+FhN2bmp/S2+9fT9Rjw60aEO+cnT8bF2TH6FBLD1LWSgu0WhQDsqI9rGiamyHxuuwTGcWqUQwAUIs/wrHi1gKrTDGrCqsh+K5K/CcAAP//FP712Xu+8jIAAAAASUVORK5CYII=", sa = {
  data() {
    return {};
  }
}, yt = window.Vue.createElementVNode, oa = window.Vue.openBlock, ia = window.Vue.createElementBlock, aa = { class: "text-center" };
function la(e, t, n, r, s, o) {
  return oa(), ia("div", aa, t[0] || (t[0] = [
    yt("h2", null, "404 Not found", -1),
    yt("img", { src: sr }, null, -1),
    yt("h4", null, "Sorry, we couldn't find the url you're looking for!", -1)
  ]));
}
const ca = /* @__PURE__ */ Ie(sa, [["render", la]]), Nt = ms({
  history: Wr(),
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
      component: ca
    }
  ]
}), ua = {
  name: "app",
  router: Nt,
  props: ["apiUrl", "readOnly"],
  data() {
    return {
      lang: "fr"
    };
  },
  methods: {
    changeLang: function() {
      window.Quasar.lang.set(this.lang == "fr" ? window.Quasar.lang.fr : window.Quasar.lang.en);
    }
  }
}, pn = window.Vue.createElementVNode, te = window.Vue.resolveComponent, de = window.Vue.withCtx, ee = window.Vue.createVNode, mn = window.Vue.createTextVNode, da = window.Vue.openBlock, fa = window.Vue.createBlock;
function ha(e, t, n, r, s, o) {
  const i = te("q-avatar"), c = te("q-toolbar-title"), u = te("q-space"), d = te("q-btn-toggle"), a = te("q-btn"), l = te("q-toolbar"), m = te("q-header"), w = te("router-view"), g = te("q-page-container"), b = te("q-footer"), v = te("q-layout");
  return da(), fa(v, { view: "hHh LpR fFf" }, {
    default: de(() => [
      ee(m, {
        elevated: "",
        class: "text-white gradient-bg"
      }, {
        default: de(() => [
          ee(l, { class: "" }, {
            default: de(() => [
              ee(c, null, {
                default: de(() => [
                  pn("div", null, [
                    ee(i, {
                      onClick: t[0] || (t[0] = (O) => e.$router.push("/"))
                    }, {
                      default: de(() => t[3] || (t[3] = [
                        pn("img", { src: sr }, null, -1)
                      ])),
                      _: 1,
                      __: [3]
                    }),
                    t[4] || (t[4] = mn("Orchestra UI "))
                  ])
                ]),
                _: 1
              }),
              ee(u),
              ee(d, {
                modelValue: s.lang,
                "onUpdate:modelValue": t[1] || (t[1] = (O) => s.lang = O),
                options: [
                  { label: "Fr", value: "fr" },
                  { label: "En", value: "en" }
                ],
                onInput: t[2] || (t[2] = (O) => o.changeLang())
              }, null, 8, ["modelValue"]),
              ee(a, {
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
      ee(g, null, {
        default: de(() => [
          ee(w, {
            "api-url": n.apiUrl,
            "orchestra-ui-read-only": n.readOnly
          }, null, 8, ["api-url", "orchestra-ui-read-only"])
        ]),
        _: 1
      }),
      ee(b, {
        elevated: "",
        class: "bg-grey-8 text-white"
      }, {
        default: de(() => [
          ee(l, null, {
            default: de(() => [
              ee(c, { class: "absolute-center" }, {
                default: de(() => t[5] || (t[5] = [
                  mn(" Copyright © 2020 - 2024 ")
                ])),
                _: 1,
                __: [5]
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
const pa = /* @__PURE__ */ Ie(ua, [["render", ha]]), ma = {
  name: "app",
  router: Nt,
  data() {
    return {
      left: !1
    };
  },
  props: ["apiUrl", "readOnly"]
}, ga = window.Vue.resolveComponent, wa = window.Vue.createVNode, ya = window.Vue.openBlock, va = window.Vue.createElementBlock;
function ba(e, t, n, r, s, o) {
  const i = ga("router-view");
  return ya(), va("div", null, [
    wa(i, {
      "api-url": n.apiUrl,
      "orchestra-ui-read-only": n.readOnly
    }, null, 8, ["api-url", "orchestra-ui-read-only"])
  ]);
}
const _a = /* @__PURE__ */ Ie(ma, [["render", ba]]), Ea = {
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
}, Ra = {
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
};
var xa = {
  install: function(e, t) {
    e.use(Nt), e.component("vui-orchestra-standalone", pa), e.component("vui-orchestra", _a), e.component("vui-orchestra-home", nr), e.component("vui-orchestra-process", rr), VertigoUi.lang.enUS.vuiOrchestra = Ra, VertigoUi.lang.fr.vuiOrchestra = Ea;
  }
};
window.VertigoOrchestraUi = xa;
export {
  xa as default
};
//# sourceMappingURL=vertigo-orchestra-ui.es.js.map
