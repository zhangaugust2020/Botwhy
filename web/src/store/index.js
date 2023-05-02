import { createStore } from 'vuex'
import createPersistedstate from 'vuex-persistedstate'
import ModuleUser from './user'
import ModulePk from './pk'
import ModuleRecord from './record'
export default createStore({
  state: {
  },
  mutations: {
  },
  actions: {
  },
  modules: {
    user: ModuleUser,
    pk: ModulePk,
    record: ModuleRecord,
  },
  plugins: [
    createPersistedstate({
      key: 'save',
      paths: ['record', 'user']
    })
  ]
})
