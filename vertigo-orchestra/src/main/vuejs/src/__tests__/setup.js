import { vi } from 'vitest'
import EnUs from '../lang/vertigo-orchestra-en.js'
import Fr from '../lang/vertigo-orchestra-fr.js'

// Set up window.Quasar with real date utilities + lang
globalThis.window = globalThis.window || {}
globalThis.window.Quasar = {
  lang: {
    enUS: { vuiOrchestra: EnUs.orchestra },
    fr: { vuiOrchestra: Fr.orchestra },
    set: vi.fn()
  },
  notify: vi.fn(),
  date: {
    extractDate(str, format) {
      const parts = str.match(/(\d{4})-(\d{2})-(\d{2})T(\d{2}):(\d{2})/)
      if (!parts) return 0
      return Date.UTC(parts[1], parts[2] - 1, parts[3], parts[4], parts[5])
    },
    formatDate(timestamp, format) {
      const d = new Date(timestamp)
      const pad = n => String(n).padStart(2, '0')
      const m = {
        DD: pad(d.getUTCDate()),
        MM: pad(d.getUTCMonth() + 1),
        YYYY: String(d.getUTCFullYear()),
        HH: pad(d.getUTCHours()),
        mm: pad(d.getUTCMinutes())
      }
      return format.replace(/DD|MM|YYYY|HH|mm/g, k => m[k])
    },
    addToDate(date, delta) {
      return new Date(date + (delta.days || 0) * 86400000).getTime()
    },
    subtractFromDate(date, delta) {
      return new Date(date - (delta.days || 0) * 86400000).getTime()
    },
    getDayOfWeek(date) {
      const day = new Date(date).getUTCDay()
      return day || 7
    }
  }
}
globalThis.Quasar = globalThis.window.Quasar

// Inject $vui globally — components call $vui.i18n().vuiOrchestra.orchestra.xxx
globalThis.$vui = {
  i18n: () => ({
    vuiOrchestra: {
      orchestra: {
        ...EnUs.orchestra,
        ...Fr.orchestra
      }
    }
  })
}