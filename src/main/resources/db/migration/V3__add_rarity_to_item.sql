-- Add rarity field to items
ALTER TABLE item ADD COLUMN rarity VARCHAR(20) NOT NULL DEFAULT 'COMMON';