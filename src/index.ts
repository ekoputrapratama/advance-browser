import { registerPlugin } from '@capacitor/core';

import type { AdvanceBrowserPlugin } from './definitions';

const AdvanceBrowser = registerPlugin<AdvanceBrowserPlugin>('AdvanceBrowser', {
  web: () => import('./web').then(m => new m.AdvanceBrowserWeb()),
});

export * from './definitions';
export { AdvanceBrowser };
