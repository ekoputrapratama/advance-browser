import { WebPlugin } from '@capacitor/core';

import type { AdvanceBrowserPlugin } from './definitions';

export class AdvanceBrowserWeb extends WebPlugin implements AdvanceBrowserPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
