import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import NotFound from '../views/NotFound.vue'

describe('NotFound', () => {
  it('renders 404 message', () => {
    const wrapper = mount(NotFound)
    expect(wrapper.find('h2').text()).toBe('404 Not found')
  })

  it('renders apology message', () => {
    const wrapper = mount(NotFound)
    expect(wrapper.find('h4').text()).toContain("couldn't find the url")
  })

  it('renders logo image', () => {
    const wrapper = mount(NotFound)
    const img = wrapper.find('img')
    expect(img.exists()).toBe(true)
  })
})