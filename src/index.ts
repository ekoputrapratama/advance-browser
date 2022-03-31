import { registerPlugin } from '@capacitor/core';

import type { AdvanceBrowserWeb } from './web';

const AdvanceBrowser = registerPlugin<AdvanceBrowserWeb>('AdvanceBrowser', {
  web: () => import('./web').then(m => new m.AdvanceBrowserWeb()),
});

export * from './definitions';
export { AdvanceBrowser };
