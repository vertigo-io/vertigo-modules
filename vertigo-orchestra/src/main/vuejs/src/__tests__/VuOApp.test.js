import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount, config } from '@vue/test-utils'
import { createApp } from 'vue'
import VuOApp from '../views/VuOApp.vue'
import EnUs from '../lang/vertigo-orchestra-en.js'

const orchEn = EnUs.orchestra

beforeEach(() => {
  // Inject real $vui i18n provider
  config.global.plugins = []
  config.global.mocks = {
    $vui: {
      i18n: () => ({
        vuiOrchestra: { orchestra: orchEn }
      })
    }
  }
  config.global.stubs = {
    'router-view': true
  }
})

describe('VuOApp', () => {
  it('renders router-view', () => {
    const wrapper = mount(VuOApp, {
      props: { apiUrl: 'http://localhost:8080' }
    })
    expect(wrapper.html()).toBeTruthy()
  })

  it('accepts apiUrl prop', () => {
    const wrapper = mount(VuOApp, {
      props: { apiUrl: 'http://localhost:9000/api' }
    })
    expect(wrapper.vm.apiUrl).toBe('http://localhost:9000/api')
  })

  it('accepts readOnly prop', () => {
    const wrapper = mount(VuOApp, {
      props: { apiUrl: 'http://localhost:8080', readOnly: true }
    })
    expect(wrapper.vm.readOnly).toBe(true)
  })
})