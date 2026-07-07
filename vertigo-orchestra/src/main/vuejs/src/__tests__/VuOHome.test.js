import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount, config } from '@vue/test-utils'
import VuOHome from '../views/VuOHome.vue'

const quasarStubs = {
  'q-table': true, 'q-tabs': true, 'q-tab': true,
  'q-btn': true, 'q-tr': true, 'q-td': true,
  'q-spinner': true, 'q-icon': true
}

beforeEach(() => {
  vi.clearAllMocks()
})

function mountHome() {
  return mount(VuOHome, {
    global: {
      mocks: {
        $vui: globalThis.$vui,
        $q: globalThis.window.Quasar
      },
      stubs: quasarStubs
    },
    props: { apiUrl: 'http://test' }
  })
}

describe('VuOHome - data initialization', () => {
  it('status defaults to A', () => {
    expect(mountHome().vm.status).toBe('A')
  })

  it('offset defaults to 0', () => {
    expect(mountHome().vm.offset).toBe(0)
  })

  it('tab defaults to all', () => {
    expect(mountHome().vm.tab).toBe('all')
  })

  it('columns has 4 fields', () => {
    expect(mountHome().vm.columns.length).toBe(4)
  })

  it('column names are correct', () => {
    const names = mountHome().vm.columns.map(c => c.name)
    expect(names).toEqual(['processLabel', 'state', 'lastExecutionTime', 'nextExecutionTime'])
  })

  it('data defaults to empty array', () => {
    expect(Array.isArray(mountHome().vm.data)).toBe(true)
  })

  it('loading and fail default to false', () => {
    const w = mountHome()
    // loading=true is set synchronously by getWeek() in created(), resolves after axios tick
    expect(w.vm.fail).toBe(false)
  })
})

describe('VuOHome - formatDate', () => {
  it('converts ISO to DD/MM/YYYY', () => {
    const w = mountHome()
    const result = w.vm.formatDate('2026-07-06T14:30:00Z')
    expect(result).toMatch(/^\d{2}\/\d{2}\/\d{4}$/)
  })
})

describe('VuOHome - getWeekLimits', () => {
  it('returns formatted start/end dates', () => {
    const w = mountHome()
    const limits = w.vm.getWeekLimits(0)
    expect(typeof limits.startOfWeek).toBe('string')
    expect(typeof limits.endOfWeek).toBe('string')
  })

  it('offset shifts dates', () => {
    const w = mountHome()
    const now = w.vm.getWeekLimits(0)
    const prev = w.vm.getWeekLimits(-1)
    const next = w.vm.getWeekLimits(1)
    expect(prev.startOfWeek).not.toBe(now.startOfWeek)
    expect(next.startOfWeek).not.toBe(now.startOfWeek)
  })
})

describe('VuOHome - getWeek navigation', () => {
  it('getWeek(0) resets offset', () => {
    const w = mountHome()
    w.vm.offset = 5
    w.vm.getWeek(0)
    expect(w.vm.offset).toBe(0)
  })

  it('getWeek(-1) decrements offset', () => {
    const w = mountHome()
    w.vm.offset = 0
    w.vm.getWeek(-1)
    expect(w.vm.offset).toBe(-1)
  })

  it('getWeek(1) increments offset', () => {
    const w = mountHome()
    w.vm.offset = 0
    w.vm.getWeek(1)
    expect(w.vm.offset).toBe(1)
  })

  it('getWeek sets start/end of week dates', () => {
    const w = mountHome()
    w.vm.getWeek(0)
    expect(w.vm.startOfWeek).toMatch(/^\d{2}\/\d{2}\/\d{4}$/)
    expect(w.vm.endOfWeek).toMatch(/^\d{2}\/\d{2}\/\d{4}$/)
  })
})

describe('VuOHome - getStatus', () => {
  it('sets status to SUCCESS', () => {
    const w = mountHome()
    w.vm.getStatus('SUCCESS')
    expect(w.vm.status).toBe('SUCCESS')
  })

  it('sets status to ERROR', () => {
    const w = mountHome()
    w.vm.getStatus('ERROR')
    expect(w.vm.status).toBe('ERROR')
  })

  it('sets status to MISFIRED', () => {
    const w = mountHome()
    w.vm.getStatus('MISFIRED')
    expect(w.vm.status).toBe('MISFIRED')
  })

  it('sets status to A', () => {
    const w = mountHome()
    w.vm.status = 'ERROR'
    w.vm.getStatus('A')
    expect(w.vm.status).toBe('A')
  })
})