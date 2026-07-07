import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount, config } from '@vue/test-utils'
import VuOProcess from '../views/VuOProcess.vue'

const quasarStubs = {
  'q-splitter': true,
  'q-card': true,
  'q-card-section': true,
  'q-separator': true,
  'q-list': true,
  'q-item': true,
  'q-item-section': true,
  'q-btn': true,
  'q-icon': true,
  'q-tab': true,
  'q-tabs': true,
  'q-tab-panel': true,
  'q-tab-panels': true,
  'q-infinite-scroll': true,
  'q-expansion-item': true,
  'q-avatar': true,
  'q-tooltip': true,
  'q-spinner-ios': true,
  'q-form': true,
  'q-input': true,
  'q-radio': true
}

beforeEach(() => {
  vi.clearAllMocks()
})

function mountProcess(overrides = {}) {
  return mount(VuOProcess, {
    global: {
      mocks: {
        $vui: globalThis.$vui,
        $q: globalThis.window.Quasar
      },
      stubs: quasarStubs
    },
    props: {
      apiUrl: 'http://test',
      processName: 'test',
      orchestraUiReadOnly: false,
      ...overrides
    }
  })
}

describe('VuOProcess - status icon mapping', () => {
  it('DONE -> done', () => {
    expect(mountProcess().vm.getIconFromExecutionState('DONE')).toBe('done')
  })

  it('ERROR -> error', () => {
    expect(mountProcess().vm.getIconFromExecutionState('ERROR')).toBe('error')
  })

  it('ABORTED -> flash_on', () => {
    expect(mountProcess().vm.getIconFromExecutionState('ABORTED')).toBe('flash_on')
  })

  it('unknown -> help', () => {
    const w = mountProcess()
    expect(w.vm.getIconFromExecutionState('UNKNOWN')).toBe('help')
    expect(w.vm.getIconFromExecutionState(null)).toBe('help')
    expect(w.vm.getIconFromExecutionState('')).toBe('help')
  })
})

describe('VuOProcess - status color mapping', () => {
  it('DONE -> green', () => {
    expect(mountProcess().vm.getColorFromExecutionState('DONE')).toBe('green')
  })

  it('ERROR -> red', () => {
    expect(mountProcess().vm.getColorFromExecutionState('ERROR')).toBe('red')
  })

  it('ABORTED -> orange', () => {
    expect(mountProcess().vm.getColorFromExecutionState('ABORTED')).toBe('orange')
  })

  it('unknown -> grey', () => {
    const w = mountProcess()
    expect(w.vm.getColorFromExecutionState('UNKNOWN')).toBe('grey')
    expect(w.vm.getColorFromExecutionState(null)).toBe('grey')
  })
})

describe('VuOProcess - formatDate', () => {
  it('ISO -> DD/MM/YYYY HH:mm', () => {
    const w = mountProcess()
    const result = w.vm.formatDate('2026-07-06T14:30:00Z')
    expect(result).toMatch(/^\d{2}\/\d{2}\/\d{4} \d{2}:\d{2}$/)
  })

  it('null -> empty', () => {
    const w = mountProcess()
    expect(w.vm.formatDate(null)).toBe('')
    expect(w.vm.formatDate(undefined)).toBe('')
  })
})

describe('VuOProcess - data defaults', () => {
  it('editMode defaults false', () => {
    const w = mountProcess()
    expect(w.vm.editMode.technical).toBe(false)
    expect(w.vm.editMode.settings).toBe(false)
  })

  it('processInfo defaults to empty object', () => {
    expect(mountProcess().vm.processInfo).toEqual({})
  })

  it('executions defaults to empty array', () => {
    const w = mountProcess()
    expect(Array.isArray(w.vm.executions)).toBe(true)
    expect(w.vm.executions.length).toBe(0)
  })

  it('splitterModel defaults to 50', () => {
    expect(mountProcess().vm.splitterModel).toBe(50)
  })

  it('splitterModelExecutions defaults to 20', () => {
    expect(mountProcess().vm.splitterModelExecutions).toBe(20)
  })

  it('limit defaults to 20', () => {
    expect(mountProcess().vm.limit).toBe(20)
  })

  it('filterTab defaults to all', () => {
    expect(mountProcess().vm.filterTab).toBe('all')
  })
})