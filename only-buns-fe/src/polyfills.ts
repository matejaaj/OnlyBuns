(window as any).global = window;
(window as any).process = { env: {} };
import { Buffer } from 'buffer';
(window as any).Buffer = Buffer;
